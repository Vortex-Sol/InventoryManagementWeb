const fmtCurrency = (n) => {
    if (n === null || n === undefined || n === '') return '';
    const num = Number(n);
    if (Number.isNaN(num)) return n;
    return new Intl.NumberFormat(undefined, { style: 'currency', currency: 'USD' }).format(num);
};
const fmtDateTime = (value) => {
    if (!value) return '';
    const d = new Date(value);
    if (isNaN(d.getTime())) return value;
    return d.toLocaleString();
};
const getCellFormatter = (key) => {
    const k = (key || '').toLowerCase();
    if (/(amount|total|price|cost|sum|revenue|net)/.test(k)) return fmtCurrency;
    if (/(date|time|created|updated|issued|timestamp)/.test(k)) return fmtDateTime;
    return (v) => (typeof v === 'object' && v !== null) ? JSON.stringify(v) : (v ?? '');
};
function renderTable(containerTable, data) {
    const table = containerTable, thead = table.querySelector('thead'), tbody = table.querySelector('tbody');
    thead.innerHTML = ''; tbody.innerHTML = '';
    if (!Array.isArray(data) || data.length === 0) {
        table.classList.add('d-none');
        const empty = document.createElement('div');
        empty.className = 'empty-state'; empty.textContent = 'No records to display.';
        table.parentElement.querySelectorAll('.empty-state').forEach(e => e.remove());
        table.parentElement.appendChild(empty);
        return;
    }
    const columns = Array.from(data.reduce((set, row) => { Object.keys(row || {}).forEach(k => set.add(k)); return set; }, new Set()));
    const trHead = document.createElement('tr');
    columns.forEach(c => {
        const th = document.createElement('th');
        th.textContent = c.replace(/_/g,' ').replace(/\b\w/g, s => s.toUpperCase());
        trHead.appendChild(th);
    });
    thead.appendChild(trHead);
    const formatters = columns.map(getCellFormatter);
    data.forEach(row => {
        const tr = document.createElement('tr');
        columns.forEach((c, idx) => {
            const td = document.createElement('td');
            const v = row?.[c];
            td.innerHTML = `<span class="truncate" title="${v ?? ''}">${formatters[idx](v)}</span>`;
            tr.appendChild(td);
        });
        tbody.appendChild(tr);
    });
    table.classList.remove('d-none');
    table.parentElement.querySelectorAll('.empty-state').forEach(e => e.remove());
}
function paginate(data, pageSize, page) {
    const total = data.length;
    const pages = Math.max(1, Math.ceil(total / pageSize));
    const p = Math.min(Math.max(1, page), pages);
    const start = (p - 1) * pageSize;
    return { slice: data.slice(start, start + pageSize), page: p, pages, total };
}
function renderPager(pagerUl, pages, current, onGo) {
    pagerUl.innerHTML = '';
    if (pages <= 1) return;
    const add = (label, p, disabled = false, active = false) => {
        const li = document.createElement('li');
        li.className = `page-item ${disabled ? 'disabled' : ''} ${active ? 'active' : ''}`;
        const a = document.createElement('a'); a.className = 'page-link'; a.href = '#'; a.textContent = label;
        a.onclick = (e) => { e.preventDefault(); if (!disabled) onGo(p); };
        li.appendChild(a); pagerUl.appendChild(li);
    };
    add('«', 1, current === 1);
    add('‹', Math.max(1, current - 1), current === 1);
    for (let p = 1; p <= pages; p++) add(String(p), p, false, p === current);
    add('›', Math.min(pages, current + 1), current === pages);
    add('»', pages, current === pages);
}
function downloadCSV(filename, rows) {
    if (!rows || !rows.length) return;
    const cols = Array.from(rows.reduce((s, r) => { Object.keys(r).forEach(k => s.add(k)); return s; }, new Set()));
    const esc = (v) => {
        if (v === null || v === undefined) return '';
        let s = String(v);
        if (/[",\n]/.test(s)) s = '"' + s.replace(/"/g,'""') + '"';
        return s;
    };
    const csv = [cols.join(',')].concat(rows.map(r => cols.map(c => esc(r[c])).join(','))).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a'); a.href = url; a.download = filename; a.click();
    URL.revokeObjectURL(url);
}
function showAlert(container, type, msg) {
    container.innerHTML = `<div class="alert alert-${type} py-2 mb-2">${msg}</div>`;
}
function setLoading(spinner, on) { spinner.style.display = on ? 'inline-block' : 'none'; }
function defaultDateTimes() {
    const now = new Date();
    const pad = (n) => String(n).padStart(2,'0');
    const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
    const end   = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
    const toLocalInput = (d) => `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    const rs = document.getElementById('receiptsStart'); if (rs) rs.value = toLocalInput(start);
    const re = document.getElementById('receiptsEnd');   if (re) re.value = toLocalInput(end);
    const es = document.getElementById('employeesStart'); if (es) es.value = toLocalInput(start);
    const ee = document.getElementById('employeesEnd');   if (ee) ee.value = toLocalInput(end);
}

const r_fmtMoney = fmtCurrency;
const r_fmtDateTime = fmtDateTime;

function r_extractReceipts(json) {
    const data = json?.data || {};
    const list = Array.isArray(data.receiptsData) ? data.receiptsData : [];
    const receipts = list.map(x => x?.receipt ? x.receipt : null).filter(Boolean);
    const totalSum = data.totalPriceReceipts ?? 0;
    return { receipts, totalSum };
}
function r_renderReceiptCard(r) {
    const header = r.header || {};
    const items = Array.isArray(r.items) ? r.items : [];
    const pays = Array.isArray(r.payments) ? r.payments : [];
    const pay = pays[0] || {};
    const cancelled = r.cancellation;

    const rows = items.map(it => {
        const qty = Number(it.quantity || 0);
        const price = Number(it.price || 0);
        const line = qty * price;
        return `
          <tr>
            <td class="truncate" title="${it.name ?? ''}">${it.name ?? ''}</td>
            <td>${qty}</td>
            <td>${it.unit ?? ''}</td>
            <td>${r_fmtMoney(price)}</td>
            <td>${it.vatRate ?? ''}</td>
            <td>${r_fmtMoney(line)}</td>
          </tr>`;
    }).join('');

    const computedTotal = items.reduce((a, it) => a + Number(it.quantity || 0) * Number(it.price || 0), 0);

    return `
        <div class="card mini-card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <div>
              <strong>Cashier:</strong> ${header.cashier ?? '-'}
              <span class="ms-2 pill">Operator: ${header.operator ?? '-'}</span>
              ${header.invoice ? '<span class="ms-2 badge text-bg-info">Invoice</span>' : ''}
            </div>
            <div>
              ${cancelled ? `<span class="badge text-bg-danger">Cancelled by ${cancelled.cancelledBy ?? '-'} at ${r_fmtDateTime(cancelled.cancelledAt)}</span>` : ''}
            </div>
          </div>
          <div class="card-body">
            <div class="table-responsive mb-2">
              <table class="table table-sm table-striped align-middle">
                <thead>
                  <tr><th>Item</th><th>Qty</th><th>Unit</th><th>Price</th><th>VAT</th><th>Line Total</th></tr>
                </thead>
                <tbody>${rows || '<tr><td colspan="6" class="text-muted">No items</td></tr>'}</tbody>
                <tfoot>
                  <tr>
                    <th colspan="5" class="text-end">Receipt Total:</th>
                    <th>${r_fmtMoney(pay.amount ?? computedTotal)}</th>
                  </tr>
                </tfoot>
              </table>
            </div>

            <div class="row g-3">
              <div class="col-md-6">
                <div class="border rounded p-2">
                  <div><strong>Payment:</strong> ${(pay.type || '').toString().toUpperCase()}</div>
                  <div><strong>Amount:</strong> ${r_fmtMoney(pay.amount ?? computedTotal)}</div>
                  ${pay.amountReceived !== undefined ? `<div><strong>Received:</strong> ${r_fmtMoney(pay.amountReceived)}</div>` : ''}
                  ${pay.changeGiven !== undefined ? `<div><strong>Change:</strong> ${r_fmtMoney(pay.changeGiven)}</div>` : ''}
                </div>
              </div>
              <div class="col-md-6">
                <div class="border rounded p-2">
                  <div><strong>Message:</strong> ${r.footer?.message ?? ''}</div>
                </div>
              </div>
            </div>
          </div>
        </div>`;
}
function r_renderPaymentBreakdown(receipts) {
    const counts = {}, sums = {};
    receipts.forEach(r => {
        const pay = Array.isArray(r.payments) ? r.payments[0] : null;
        const method = (pay?.type || 'unknown').toString().toLowerCase();
        const amount = Number(pay?.amount ?? 0);
        counts[method] = (counts[method] || 0) + 1;
        sums[method] = (sums[method] || 0) + amount;
    });
    const chips = Object.keys(counts).sort().map(k =>
        `<span class="pill">${k.toUpperCase()}: <strong>${counts[k]} / ${fmtCurrency(sums[k])}</strong></span>`
    ).join(' ');
    return `<div class="d-flex gap-2 flex-wrap">
                <span class="pill">Payment Breakdown</span>
                ${chips || '<span class="text-muted">No payments</span>'}
              </div>`;
}
function r_renderReceiptsSummary(totalCount, totalSum) {
    return `<div class="d-flex gap-2 flex-wrap">
                <span class="pill">Receipts: <strong>${totalCount}</strong></span>
                <span class="pill">Total Turnover: <strong>${fmtCurrency(totalSum || 0)}</strong></span>
              </div>`;
}
function r_exportReceiptsCSV(receipts) {
    const rows = [];
    receipts.forEach(r => {
        const cashier = r.header?.cashier ?? '';
        const operator = r.header?.operator ?? '';
        const invoice = !!r.header?.invoice;
        const cancelled = !!r.cancellation;
        const cancelledAt = r.cancellation?.cancelledAt ?? '';
        const cancelledBy = r.cancellation?.cancelledBy ?? '';
        const payment = Array.isArray(r.payments) ? r.payments[0] : {};
        const payType = payment?.type ?? '';
        const payAmount = payment?.amount ?? '';
        const payReceived = payment?.amountReceived ?? '';
        const payChange = payment?.changeGiven ?? '';
        const items = Array.isArray(r.items) ? r.items : [];
        if (!items.length) {
            rows.push({
                cashier, operator, invoice, cancelled, cancelledAt, cancelledBy,
                itemName: '', quantity: '', unit: '', price: '', vatRate: '',
                paymentType: payType, paymentAmount: payAmount, amountReceived: payReceived, changeGiven: payChange
            });
        } else {
            items.forEach(it => rows.push({
                cashier, operator, invoice, cancelled, cancelledAt, cancelledBy,
                itemName: it.name ?? '', quantity: it.quantity ?? '', unit: it.unit ?? '', price: it.price ?? '', vatRate: it.vatRate ?? '',
                paymentType: payType, paymentAmount: payAmount, amountReceived: payReceived, changeGiven: payChange
            }));
        }
    });
    const cols = ['cashier','operator','invoice','cancelled','cancelledAt','cancelledBy','itemName','quantity','unit','price','vatRate','paymentType','paymentAmount','amountReceived','changeGiven'];
    const esc = (v) => { if (v === null || v === undefined) return ''; let s = String(v); if (/[",\n]/.test(s)) s = '"' + s.replace(/"/g,'""') + '"'; return s; };
    const csv = [cols.join(',')].concat(rows.map(r => cols.map(c => esc(r[c])).join(','))).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a'); a.href = url; a.download = 'receipts.csv'; a.click();
    URL.revokeObjectURL(url);
}
function r_paginate(arr, pageSize, page) {
    const total = arr.length;
    const pages = Math.max(1, Math.ceil(total / pageSize));
    const p = Math.min(Math.max(1, page), pages);
    const start = (p - 1) * pageSize;
    return { slice: arr.slice(start, start + pageSize), page: p, pages, total };
}
function r_renderPager(pagerUl, pages, current, onGo) {
    pagerUl.innerHTML = '';
    if (pages <= 1) return;
    const add = (label, p, disabled = false, active = false) => {
        const li = document.createElement('li');
        li.className = `page-item ${disabled ? 'disabled' : ''} ${active ? 'active' : ''}`;
        const a = document.createElement('a'); a.className = 'page-link'; a.href = '#'; a.textContent = label;
        a.onclick = (e) => { e.preventDefault(); if (!disabled) onGo(p); };
        li.appendChild(a); pagerUl.appendChild(li);
    };
    add('«', 1, current === 1);
    add('‹', Math.max(1, current - 1), current === 1);
    for (let p = 1; p <= pages; p++) add(String(p), p, false, p === current);
    add('›', Math.min(pages, current + 1), current === pages);
    add('»', pages, current === pages);
}

function extractArray(data) {
    if (Array.isArray(data)) return data;
    if (data && typeof data === 'object') {
        const firstArrayKey = Object.keys(data).find(k => Array.isArray(data[k]));
        if (firstArrayKey) return data[firstArrayKey];
    }
    return [];
}
function computeSummary(data, title) {
    const arr = extractArray(data);
    const totalItems = arr.length;
    const sumKeys = ['total', 'amount', 'sum', 'revenue', 'price', 'cost', 'net', 'gross'];
    const sums = {};
    arr.forEach(r => {
        Object.keys(r || {}).forEach(k => {
            if (sumKeys.some(s => k.toLowerCase().includes(s)) && typeof r[k] === 'number') {
                sums[k] = (sums[k] || 0) + r[k];
            }
        });
    });
    const pills = [`<span class="pill">Items: <strong>${totalItems}</strong></span>`]
        .concat(Object.keys(sums).map(k => `<span class="pill">${k}: <strong>${fmtCurrency(sums[k])}</strong></span>`));
    return `<div class="d-flex gap-2 flex-wrap"><span class="pill">${title}</span>${pills.join(' ')}</div>`;
}
function attachSection({ btnTodayId, btnPeriodId, startId, endId, urlToday, urlPeriod,
                           spinnerId, alertId, summaryId, tableId, pagerId, exportBtnId,
                           pageSize = 15, summaryTitle }) {
    const spinner = document.getElementById(spinnerId);
    const alertEl = document.getElementById(alertId);
    const summaryEl = document.getElementById(summaryId);
    const table = document.getElementById(tableId);
    const pager = document.getElementById(pagerId);
    const exportBtn = document.getElementById(exportBtnId);
    let currentRows = [];
    let currentPage = 1;

    function go(page = 1) {
        const { slice, pages } = paginate(currentRows, pageSize, page);
        renderTable(table, slice);
        currentPage = page;
        renderPager(pager, pages, currentPage, (p) => go(p));
    }
    function handleData(json) {
        if (!json?.success) {
            showAlert(alertEl, 'warning', json?.message || 'No data found.');
            table.classList.add('d-none'); summaryEl.classList.add('d-none'); return;
        }
        const data = json.data;
        currentRows = extractArray(data);
        if (!currentRows.length) {
            showAlert(alertEl, 'secondary', 'Nothing to display for the selected criteria.');
            table.classList.add('d-none'); summaryEl.classList.add('d-none'); return;
        }
        alertEl.innerHTML = '';
        summaryEl.classList.remove('d-none');
        summaryEl.innerHTML = computeSummary(data, summaryTitle);
        go(1);
    }
    async function fetchUrl(url) {
        try {
            setLoading(spinner, true); alertEl.innerHTML = '';
            const res = await fetch(url, { headers: { 'Accept': 'application/json' }});
            const json = await res.json(); handleData(json);
        } catch {
            showAlert(alertEl, 'danger', 'Error fetching report.');
        } finally { setLoading(spinner, false); }
    }
    const btnToday = document.getElementById(btnTodayId);
    if (btnToday) btnToday.addEventListener('click', () => fetchUrl(urlToday));
    const btnPeriod = document.getElementById(btnPeriodId);
    if (btnPeriod && startId && endId) {
        btnPeriod.addEventListener('click', () => {
            const start = document.getElementById(startId).value;
            const end = document.getElementById(endId).value;
            const fullUrl = `${urlPeriod}?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`;
            fetchUrl(fullUrl);
        });
    }
    if (exportBtn) {
        exportBtn.addEventListener('click', () => {
            if (!currentRows.length) { showAlert(alertEl, 'secondary', 'No data to export.'); return; }
            downloadCSV(`${summaryTitle.toLowerCase().replace(/\s+/g,'-')}.csv`, currentRows);
        });
    }
}
function attachEmployeesSection() {
    const spinner = document.getElementById('spinEmployees');
    const alertEl = document.getElementById('employeesAlert');
    const summaryEl = document.getElementById('employeesSummary');
    const cardsEl = document.getElementById('employeesCards');
    const pagerEl = document.getElementById('employeesPager');
    const exportBtn = document.getElementById('exportEmployees');

    let allEmployees = []
    let currentPage = 1;
    const pageSize = 4;

    function empQuickStats(emp) {
        const logins = Array.isArray(emp.loginAudits) ? emp.loginAudits.length : 0;
        const logouts = Array.isArray(emp.logoutAudits) ? emp.logoutAudits.length : 0;
        const receipts = Array.isArray(emp.receiptsData) ? emp.receiptsData.length : 0;
        return `
          <div class="d-flex gap-2 flex-wrap">
            <span class="pill">Logins: <strong>${logins}</strong></span>
            <span class="pill">Logouts: <strong>${logouts}</strong></span>
            <span class="pill">Receipts: <strong>${receipts}</strong></span>
          </div>`;
    }

    function renderAuditTable(rows, cols) {
        if (!Array.isArray(rows) || !rows.length) {
            return '<div class="empty-state">No records.</div>';
        }
        const head = cols.map(c => `<th>${c.label}</th>`).join('');
        const body = rows.map(r => {
            const tds = cols.map(c => {
                const v = r[c.key];
                const val = /time/i.test(c.key) ? fmtDateTime(v) : v;
                return `<td><span class="truncate" title="${val ?? ''}">${val ?? ''}</span></td>`;
            }).join('');
            return `<tr>${tds}</tr>`;
        }).join('');
        return `
          <div class="table-responsive">
            <table class="table table-sm table-striped align-middle">
              <thead><tr>${head}</tr></thead>
              <tbody>${body}</tbody>
            </table>
          </div>`;
    }

    function renderSettingsLogs(logs) {
        if (!Array.isArray(logs) || !logs.length) return '';
        const rows = logs.map(l => `
          <tr>
            <td>${l.settingsChangeAuditId ?? ''}</td>
            <td>${fmtDateTime(l.changedAt)}</td>
            <td>${l.settingId ?? ''}</td>
            <td><code class="d-block">${(typeof l.settingsChanged === 'object') ? JSON.stringify(l.settingsChanged) : (l.settingsChanged ?? '')}</code></td>
          </tr>`).join('');
        return `
          <details class="mt-2">
            <summary class="mb-2"><strong>Settings Changes (${logs.length})</strong></summary>
            <div class="table-responsive">
              <table class="table table-sm table-striped align-middle">
                <thead><tr><th>ID</th><th>Changed At</th><th>Setting ID</th><th>Changes</th></tr></thead>
                <tbody>${rows}</tbody>
              </table>
            </div>
          </details>`;
    }

    function renderEmployeeCard(emp) {
        const jobsBadges = (emp.jobs || []).map(j => `<span class="badge badge-job me-1">${j}</span>`).join('');
        const loginTable = renderAuditTable(emp.loginAudits || [], [
            { key: 'loginTime', label: 'Login Time' },
            { key: 'ipAddress', label: 'IP Address' },
            { key: 'successFailure', label: 'Result' }
        ]);
        const logoutTable = renderAuditTable(emp.logoutAudits || [], [
            { key: 'logoutTime', label: 'Logout Time' },
            { key: 'ipAddress', label: 'IP Address' },
            { key: 'reason', label: 'Reason' }
        ]);

        let receiptsHtml = '';
        if (Array.isArray(emp.receiptsData) && emp.receiptsData.length) {
            const list = emp.receiptsData
                .map(x => x?.receipt ? x.receipt : null)
                .filter(Boolean);
            receiptsHtml = `
            <details class="mt-2">
              <summary class="mb-2"><strong>Receipts (${list.length})</strong></summary>
              <div class="d-grid gap-2">
                ${list.map(r_renderReceiptCard).join('')}
              </div>
            </details>`;
        }
        const fullName = [emp.Name, emp.Surname].filter(Boolean).join(' ');
        const settingsHtml = renderSettingsLogs(emp.settingsChangeAudits || []);

        return `
          <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
              <div>
                <strong>${fullName || emp.username || ''}</strong>
                ${fullName && emp.username ? `<span class="ms-2 text-muted">@${emp.username}</span>` : ''}
                <span class="ms-2 text-muted">#${emp.employeeId ?? ''}</span>
              </div>
              <div>${jobsBadges}</div>
            </div>
            <div class="card-body">
              ${empQuickStats(emp)}
              <div class="row mt-3 g-3">
                <div class="col-md-6">
                  <h6 class="mb-2">Login Audits</h6>
                  ${loginTable}
                </div>
                <div class="col-md-6">
                  <h6 class="mb-2">Logout Audits</h6>
                  ${logoutTable}
                </div>
              </div>

              ${settingsHtml}
              ${receiptsHtml}
            </div>
          </div>`;
    }

    function drawPage(page = 1) {
        const { slice, pages } = paginate(allEmployees, pageSize, page);
        currentPage = page;
        cardsEl.innerHTML = slice.map(renderEmployeeCard).join('') || '<div class="empty-state">No employees to display.</div>';
        renderPager(pagerEl, pages, currentPage, (p) => drawPage(p));
    }

    function handleJson(json) {
        if (!json?.success) {
            showAlert(alertEl, 'warning', json?.message || 'No data found.');
            summaryEl.classList.add('d-none');
            cardsEl.innerHTML = '';
            pagerEl.innerHTML = '';
            return;
        }
        const data = json.data || {};
        const list = Array.isArray(data.employeesData) ? data.employeesData : [];
        allEmployees = list;

        if (!allEmployees.length) {
            showAlert(alertEl, 'secondary', 'Nothing to display for the selected criteria.');
            summaryEl.classList.add('d-none');
            cardsEl.innerHTML = '';
            pagerEl.innerHTML = '';
            return;
        }

        alertEl.innerHTML = '';
        const totalEmps = allEmployees.length;
        const totalReceipts = allEmployees.reduce((a, e) => a + (Array.isArray(e.receiptsData) ? e.receiptsData.length : 0), 0);
        const totalLogins = allEmployees.reduce((a, e) => a + (Array.isArray(e.loginAudits) ? e.loginAudits.length : 0), 0);
        const totalLogouts = allEmployees.reduce((a, e) => a + (Array.isArray(e.logoutAudits) ? e.logoutAudits.length : 0), 0);

        summaryEl.classList.remove('d-none');
        summaryEl.innerHTML = `
          <div class="d-flex gap-2 flex-wrap">
            <span class="pill">Employees: <strong>${totalEmps}</strong></span>
            <span class="pill">Receipts: <strong>${totalReceipts}</strong></span>
            <span class="pill">Logins: <strong>${totalLogins}</strong></span>
            <span class="pill">Logouts: <strong>${totalLogouts}</strong></span>
          </div>`;

        drawPage(1);
    }

    async function fetchUrl(url) {
        try {
            setLoading(spinner, true);
            alertEl.innerHTML = '';
            const res = await fetch(url, { headers: { 'Accept': 'application/json' }});
            const json = await res.json();
            handleJson(json);
        } catch {
            showAlert(alertEl, 'danger', 'Error fetching employee report.');
        } finally {
            setLoading(spinner, false);
        }
    }

    document.getElementById('btnEmployeesToday').addEventListener('click', () => {
        fetchUrl('/api/reports/employees/today');
    });
    document.getElementById('btnEmployeesPeriod').addEventListener('click', () => {
        const start = document.getElementById('employeesStart').value;
        const end = document.getElementById('employeesEnd').value;
        const fullUrl = `/api/reports/employees/period?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`;
        fetchUrl(fullUrl);
    });
    if (exportBtn) {
        exportBtn.addEventListener('click', () => {
            if (!allEmployees.length) { showAlert(alertEl, 'secondary', 'No data to export.'); return; }
            const rows = allEmployees.map(e => ({
                employeeId: e.employeeId,
                username: e.username,
                name: e.Name || '',
                surname: e.Surname || '',
                jobs: Array.isArray(e.jobs) ? e.jobs.join('|') : '',
                loginCount: Array.isArray(e.loginAudits) ? e.loginAudits.length : 0,
                logoutCount: Array.isArray(e.logoutAudits) ? e.logoutAudits.length : 0,
                receiptCount: Array.isArray(e.receiptsData) ? e.receiptsData.length : 0
            }));
            downloadCSV('employees-summary.csv', rows);
        });
    }
}

function attachReceiptsSection() {
    const spinner = document.getElementById('spinReceipts');
    const alertEl = document.getElementById('receiptsAlert');
    const summaryEl = document.getElementById('receiptsSummary');
    const breakdownEl = document.getElementById('receiptsPaymentBreakdown');
    const cardsEl = document.getElementById('receiptsCards');
    const pagerEl = document.getElementById('receiptsPager');
    const exportBtn = document.getElementById('exportReceipts');

    let allReceipts = [];
    const pageSize = 5;
    let currentPage = 1;
    let totalSum = 0;

    function drawPage(page = 1) {
        const { slice, pages } = r_paginate(allReceipts, pageSize, page);
        currentPage = page;
        cardsEl.innerHTML = slice.map(r_renderReceiptCard).join('') || '<div class="empty-state">No receipts to display.</div>';
        r_renderPager(pagerEl, pages, currentPage, p => drawPage(p));
    }
    function handleJson(json) {
        if (!json?.success) {
            showAlert(alertEl, 'warning', json?.message || 'No data found.');
            summaryEl.classList.add('d-none'); breakdownEl.classList.add('d-none'); cardsEl.innerHTML = ''; pagerEl.innerHTML = '';
            return;
        }
        const { receipts, totalSum: ts } = r_extractReceipts(json);
        allReceipts = receipts;
        totalSum = ts || 0;

        if (!allReceipts.length) {
            showAlert(alertEl, 'secondary', 'Nothing to display for the selected criteria.');
            summaryEl.classList.add('d-none'); breakdownEl.classList.add('d-none'); cardsEl.innerHTML = ''; pagerEl.innerHTML = '';
            return;
        }
        alertEl.innerHTML = '';
        summaryEl.classList.remove('d-none');
        summaryEl.innerHTML = r_renderReceiptsSummary(allReceipts.length, totalSum);
        breakdownEl.classList.remove('d-none');
        breakdownEl.innerHTML = r_renderPaymentBreakdown(allReceipts);
        drawPage(1);
    }
    async function fetchUrl(url) {
        try {
            setLoading(spinner, true); alertEl.innerHTML = '';
            const res = await fetch(url, { headers: { 'Accept': 'application/json' }});
            const json = await res.json(); handleJson(json);
        } catch {
            showAlert(alertEl, 'danger', 'Error fetching receipts.');
        } finally { setLoading(spinner, false); }
    }

    document.getElementById('btnReceiptsToday').addEventListener('click', () => {
        fetchUrl('/api/reports/receipts/today');
    });
    document.getElementById('btnReceiptsPeriod').addEventListener('click', () => {
        const start = document.getElementById('receiptsStart').value;
        const end = document.getElementById('receiptsEnd').value;
        const fullUrl = `/api/reports/receipts/period?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`;
        fetchUrl(fullUrl);
    });
    if (exportBtn) {
        exportBtn.addEventListener('click', () => {
            if (!allReceipts.length) { showAlert(alertEl, 'secondary', 'No data to export.'); return; }
            r_exportReceiptsCSV(allReceipts);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    defaultDateTimes();

    attachSection({
        btnTodayId: 'btnInventoryToday',
        btnPeriodId: null,
        startId: null, endId: null,
        urlToday: '/api/reports/inventory/today',
        urlPeriod: null,
        spinnerId: 'spinInventory',
        alertId: 'inventoryAlert',
        summaryId: 'inventorySummary',
        tableId: 'inventoryTable',
        pagerId: 'inventoryPager',
        exportBtnId: 'exportInventory',
        pageSize: 20,
        summaryTitle: 'Inventory'
    });
    attachEmployeesSection();
    attachReceiptsSection();
});