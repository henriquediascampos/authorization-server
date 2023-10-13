function removeRippleElementExists() {
    const rippleExists = document.querySelector('.ripple-element');
    if (rippleExists) {
        rippleExists.remove();
    }
}

function createRippleElement(event, $target) {
    const $ripple = document.createElement('div');
    $ripple.classList.add('ripple-element');
    const startingPositionRelativeToClick = $target.classList.contains(
        'starting-position-click'
    );
    if (startingPositionRelativeToClick) {
        $ripple.style.top = `${event.layerY}px`;
        $ripple.style.left = `${event.layerX}px`;
    }
    // $ripple.style.height = `${$target.offsetHeight * 2}px`;
    // $ripple.style.width = `${$target.offsetWidth * 2}px`;
    return $ripple;
}

function addRippleEffect(elementRipple) {
    elementRipple.addEventListener('mousedown', (event) => {
        removeRippleElementExists();
        const $target = event.currentTarget;
        const $ripple = createRippleElement(event, $target);

        $target.appendChild($ripple);
        setTimeout(() => {
            $ripple.classList.add('ripple-element-on');
        }, 0);
    });

    elementRipple.addEventListener('mouseup', () => {
        const rippleElement = document.querySelector('.ripple-element');
        if (rippleElement) {
            rippleElement.classList.add('ripple-element-off');
        }

        setTimeout(() => {
            removeRippleElementExists();
        }, 1500);
    });
}

function ripple() {
    Array.from(document.querySelectorAll('.ripple')).forEach(
        (elementRipple) => {
            addRippleEffect(elementRipple);
        }
    );
}

export { ripple, addRippleEffect };
