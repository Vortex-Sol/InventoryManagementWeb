import time, glob, binascii, serial

ESC = b'\x1B'
def hx(b): return binascii.hexlify(b).decode()

def calc_check(payload: bytes, start_ff: bool, include_trailing_space: bool) -> bytes:
    data = payload if include_trailing_space else payload.rstrip(b' ')
    chk = 0xFF if start_ff else 0x00
    for x in data:
        chk ^= x
    return f"{chk:02X}".encode('ascii')  # ASCII hex

def frame(payload_ascii: bytes, start_ff: bool, include_trailing_space: bool) -> bytes:
    return ESC + b'P' + payload_ascii + calc_check(payload_ascii, start_ff, include_trailing_space) + ESC + b'\\'

def ports():
    pats = ["/dev/tty.usbmodem*", "/dev/tty.usbserial*", "/dev/cu.usbmodem*", "/dev/cu.usbserial*"]
    out = []
    for p in pats: out += sorted(glob.glob(p))
    return out

def send_and_read(s, pkt, label, wait=0.25):
    s.reset_input_buffer()
    s.write(pkt); s.flush()
    time.sleep(wait)
    data = s.read(512)
    print(f"{label} TX({len(pkt)}): {hx(pkt)}")
    print(f"{label} RX({len(data)}): {hx(data)} | {data!r}")
    return data

def try_variant(dev, baud, start_ff, include_trailing_space):
    print(f"\n=== {dev}@{baud} | start={'FF' if start_ff else '00'} | space_in_xor={include_trailing_space} ===")
    with serial.Serial(dev, baudrate=baud, bytesize=8, parity='N', stopbits=1, timeout=0.6, exclusive=False) as s:
        # Line settle
        s.setDTR(False); s.setRTS(False); time.sleep(0.1)
        s.setDTR(True);  s.setRTS(True);  time.sleep(0.15)
        s.reset_input_buffer(); s.reset_output_buffer()

        # Version (no check) — some firmwares answer, some don’t:
        send_and_read(s, ESC+b'P#v'+ESC+b'\\', "ID probe", 0.25)

        # 1) Enable print on original for non-fiscal: Ps=1 → #a
        resp = send_and_read(s, frame(b"1 #a ", start_ff, include_trailing_space), "#a (enable original)")
        if not resp:
            # try asking last-error to see error code (optional)
            pass

        # 2) Quick “cash status” non-fiscal print (#t) — no CRs:
        resp_t = send_and_read(s, frame(b"0 #t ", start_ff, include_trailing_space), "#t (cash status)")
        ok = (resp_t and b"ERR" not in resp_t)

        # 3) If ok, try a minimal $w block
        if ok:
            # Start ($w): report 0, header 0
            send_and_read(s, frame(b"0;0 $w ", start_ff, include_trailing_space), "NF start $w")
            # One printable line (#: P1=0 report, P2=0 first data line) + text + CR
            line = "Hello Posnet (NF)".encode('cp852', 'replace') + b'\r'
            send_and_read(s, frame(b"0;0 $w " + line, start_ff, include_trailing_space), "NF line $w")
            # End ($w): P2=1;P3=0;P4=0
            send_and_read(s, frame(b"1;1;0;0 $w ", start_ff, include_trailing_space), "NF end $w")
        return ok

if __name__ == "__main__":
    tried_any = False
    for dev in ports():
        for baud in (9600, 115200):
            for start_ff in (False, True):                    # 0x00 then 0xFF
                for include_space in (False, True):           # exclude then include last space
                    tried_any = True
                    if try_variant(dev, baud, start_ff, include_space):
                        print("\nLooks good — one variant accepted. Stop here.")
                        raise SystemExit(0)
    if not tried_any:
        print("No USB modem/serial ports found.")
