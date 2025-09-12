document.addEventListener('DOMContentLoaded', function () {
    // --- Create form
    const formCreate = document.getElementById('warehouseFormCreate');
    if (formCreate) {
        const modeExisting = document.getElementById('modeExistingCreate');
        const modeNew = document.getElementById('modeNewCreate');
        const newBlock = document.getElementById('newRateBlockCreate');
        const existingBlock = document.getElementById('existingRateBlockCreate');

        const standardInput = document.getElementById('standard-rate');
        const existingSelect = document.getElementById('existingTaxRateIdCreate');
        const countrySelect = document.getElementById('newRateCountryCreate');

        function setModeCreate(toNew) {
            if (toNew) {
                newBlock.classList.remove('d-none');
                existingBlock.classList.add('d-none');

                if (standardInput) standardInput.setAttribute('required', 'required');
                if (countrySelect) countrySelect.setAttribute('required', 'required');

                if (existingSelect) existingSelect.removeAttribute('required');
            } else {
                newBlock.classList.add('d-none');
                existingBlock.classList.remove('d-none');

                if (standardInput) standardInput.removeAttribute('required');
                if (countrySelect) countrySelect.removeAttribute('required');
                if (existingSelect) existingSelect.setAttribute('required', 'required');
            }
        }

        setModeCreate(modeNew && modeNew.checked);

        if (modeExisting) modeExisting.addEventListener('change', () => setModeCreate(false));
        if (modeNew) modeNew.addEventListener('change', () => setModeCreate(true));

        formCreate.addEventListener('submit', function (e) {
            if (!formCreate.checkValidity()) {
                e.preventDefault();
                formCreate.reportValidity();
            }
        });
    }

    const formEdit = document.getElementById('warehouseFormEdit');
    if (formEdit) {
        const modeExistingEdit = document.getElementById('modeExistingEdit');
        const modeNewEdit = document.getElementById('modeNewEdit');
        const newBlockEdit = document.getElementById('newRateBlockEdit');
        const existingBlockEdit = document.getElementById('existingRateBlockEdit');

        const standardInputEdit = formEdit.querySelector('input[name="newRateStandardRate"]');
        const existingSelectEdit = document.getElementById('existingTaxRateIdEdit');
        const countrySelectEdit = document.getElementById('newRateCountryEdit');

        function setModeEdit(toNew) {
            if (toNew) {
                newBlockEdit.classList.remove('d-none');
                existingBlockEdit.classList.add('d-none');
                if (standardInputEdit) standardInputEdit.setAttribute('required', 'required');
                if (countrySelectEdit) countrySelectEdit.setAttribute('required', 'required');
                if (existingSelectEdit) existingSelectEdit.removeAttribute('required');
            } else {
                newBlockEdit.classList.add('d-none');
                existingBlockEdit.classList.remove('d-none');
                if (standardInputEdit) standardInputEdit.removeAttribute('required');
                if (countrySelectEdit) countrySelectEdit.removeAttribute('required');
                if (existingSelectEdit) existingSelectEdit.setAttribute('required', 'required');
            }
        }

        if (modeNewEdit && modeExistingEdit) {
            setModeEdit(modeNewEdit.checked);
            modeExistingEdit.addEventListener('change', () => setModeEdit(false));
            modeNewEdit.addEventListener('change', () => setModeEdit(true));
        }

        formEdit.addEventListener('submit', function (e) {
            if (!formEdit.checkValidity()) {
                e.preventDefault();
                formEdit.reportValidity();
            }
        });
    }
});
