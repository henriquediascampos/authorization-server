export default function button() {
    Array.from(document.querySelectorAll('.button')).forEach(
        (buttonElement) => {
            buttonElement.classList.add('ripple', 'starting-position-click');
        }
    );
    Array.from(document.querySelectorAll('.button-icon')).forEach(
        (buttonElement) => {
            buttonElement.classList.add('ripple');
        }
    );
}
