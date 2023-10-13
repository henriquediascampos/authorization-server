function checkfieldEmpty(value, inputElementForm) {
    if (value) {
        inputElementForm.parentElement.classList.add('not-empty');
    } else {
        inputElementForm.parentElement.classList.remove('not-empty');
    }
}

export default function formField() {
    Array.from(document.querySelectorAll('.form-field')).forEach(
        (elementForm) => {
            elementForm.addEventListener('click', () => {
                elementForm.querySelector('.input-form-field').focus();
            });
        }
    );

    Array.from(document.getElementsByClassName('input-form-field')).forEach(
        (inputElementForm) => {
            checkfieldEmpty(inputElementForm.value, inputElementForm);
            inputElementForm.addEventListener('input', (event) => {
                checkfieldEmpty(event.currentTarget.value, inputElementForm);
            });
        }
    );
}
