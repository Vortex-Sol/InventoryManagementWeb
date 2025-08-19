function validateDob() {
    const dob = document.getElementById('dob');
    const dobFormatted = document.getElementById('dobFormatted');
    dob.min = '1900-01-01';
    dob.max = new Date().toISOString().split('T')[0];

    document.getElementById('dobForm').addEventListener('submit', e => {
        if (!dob.value) {
            e.preventDefault();
            e.target.classList.add('was-validated');
            return;
        }

        // value is always yyyy-MM-dd
        const [y, m, d] = dob.value.split('-');
        dobFormatted.value = `${d}/${m}/${y}`; // dd/MM/yyyy
    });
}