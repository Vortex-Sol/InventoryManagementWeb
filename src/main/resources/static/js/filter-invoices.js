const dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropArea.classList.add('dragover');
});
dropArea.addEventListener('dragleave', () => {
    dropArea.classList.remove('dragover');
});
dropArea.addEventListener('drop', () => {
    dropArea.classList.remove('dragover');
});

const filterNameInput = document.getElementById('filterName');
const filterDateInput = document.getElementById('filterDate');
const sortOrderSelect = document.getElementById('sortOrder');
const resetBtn = document.getElementById('resetFilters');
const table = document.getElementById('invoiceTable');
const rows = Array.from(table.querySelectorAll('tbody tr'));

function filterAndSort() {
    let nameFilter = filterNameInput.value.toLowerCase();
    let dateFilter = filterDateInput.value;
    let sortOrder = sortOrderSelect.value;

    let filteredRows = rows.filter(row => {
        const fileName = row.querySelector('.file-name').textContent.toLowerCase();
        const fileDate = row.getAttribute('data-date');

        return (fileName.includes(nameFilter)) &&
            (!dateFilter || fileDate === dateFilter);
    });

    if(sortOrder === 'asc') {
        filteredRows.sort((a,b) => new Date(a.getAttribute('data-date')) - new Date(b.getAttribute('data-date')));
    } else if(sortOrder === 'desc') {
        filteredRows.sort((a,b) => new Date(b.getAttribute('data-date')) - new Date(a.getAttribute('data-date')));
    }

    const tbody = table.querySelector('tbody');
    tbody.innerHTML = '';
    filteredRows.forEach(row => tbody.appendChild(row));

    document.getElementById('noInvoices').style.display = filteredRows.length ? 'none' : 'block';
}

filterNameInput.addEventListener('input', filterAndSort);
filterDateInput.addEventListener('change', filterAndSort);
sortOrderSelect.addEventListener('change', filterAndSort);
resetBtn.addEventListener('click', () => {
    filterNameInput.value = '';
    filterDateInput.value = '';
    sortOrderSelect.value = '';
    filterAndSort();
});