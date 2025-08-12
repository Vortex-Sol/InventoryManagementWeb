const chk = document.getElementById('autoGenerateReport');
const view = document.getElementById('autoGenerateReportTime_view');
const hidden = document.getElementById('autoGenerateReportTime');

function syncHiddenFromView() {
    const v = view.value; // "HH:MM" или ""
    if (!v) {
        hidden.value = '';
    } else {
        hidden.value = v.length === 5 ? v + ':00' : v; // "HH:MM:SS"
    }
}

function updateState() {
    if (chk.checked) {
        view.removeAttribute('disabled');
        syncHiddenFromView();
    } else {
        view.setAttribute('disabled','disabled');
        hidden.value = ''; // отправится пустая строка -> initBinder -> null
    }
}

chk.addEventListener('change', updateState);
view.addEventListener('input', syncHiddenFromView);

// init
document.addEventListener('DOMContentLoaded', function(){
    // если thymeleaf уже вставил value в hidden (при рендере), не затираем
    // но синхронизируем начальное значение view->hidden
    syncHiddenFromView();
    updateState();
});