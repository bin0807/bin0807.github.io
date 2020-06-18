; (function (global) {
    function MyDialog(id, backdrop) {
        this.ele = document.getElementById(id);
        this.backdrop = backdrop || true;
        this.preClass = this.ele.getAttribute("class");
        this.closeEle = this.ele.querySelector('.td-modal-close');
        this.dialog = this.ele.querySelector('.td-modal-dialog')
        this.closeEle.addEventListener('click', function () {
            this.close();
        }.bind(this));
        var self = this;
        this.ele.addEventListener("click", function (e) {
            self.backdrop && e.target === this && self.close()
        })
    }
    MyDialog.prototype.open = function () {
        this.ele.style.display = 'block';
        setTimeout(function () {
            this.preClass = this.preClass + ' ' + 'in'
            this.ele.className = this.preClass;
        }.bind(this), 0)
    };
    MyDialog.prototype.close = function () {
        this.ele.style.display = 'none';
        var classList = this.preClass.split(" ")
        classList.indexOf("in") > -1 && classList.splice(classList.indexOf("in"), 1);
        var classStr = classList.join(" ");
        this.preClass = classStr;
        this.ele.className = this.preClass;
    }
    global.MyDialog = MyDialog;
})(window)