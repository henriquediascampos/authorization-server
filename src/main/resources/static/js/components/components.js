import { ripple } from './ripple.js';
import formField from './formField.js';
import button from './button.js';
import icons from './icons.js';

export default class Components {
    constructor() {
        this.icons = icons;
        this.formField = formField;
        this.button = button;
        this.ripple = ripple;
    }

    init() {
        this.icons();
        this.formField();
        this.button();
        this.ripple();
    }
}
