var homeController = (function() {
    constructor = function() {
        console.log("Controller for home active");
        this.disable = function() {
            console.log("Disabling home controller");
        }
    }
    return constructor;
}());