class CommonApp {
    static DOMAIN = location.origin;
    static API_ENDPOINT = this.DOMAIN + "/api";
    static BASE_URL_AUTHORIZATION = `${this.API_ENDPOINT}/login`;
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_ORDER = this.DOMAIN + "/api/orders";
    static BASE_URL_USER = this.DOMAIN + "/api/users";
    static BASE_URL_USER_GROUPS = this.DOMAIN + "/api/users/asgnGrp";
    static BASE_URL_AMZN_ACCOUNT = this.DOMAIN + "/api/amzn";
    static BASE_URL_GROUP = this.DOMAIN + "/api/groups";
    static BASE_URL_PRODUCT = this.DOMAIN + "/api/products";
    static BASE_URL_ANALYTICS = this.DOMAIN + "/api/analytics";
    static BASE_URL_TAG = this.DOMAIN + "/api/tags";
    static BASE_URL_FILE_UPLOAD = this.DOMAIN + "/api/file-upload";

    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'center',
                showConfirmButton: false,
                timer: 800,
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
                timer: 2000,
            })
        }

        static showChangeStatusDialog(status) {
            return Swal.fire({
                icon: `question`,
                text: `${status ? questionToActive : questionToDisable}`,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#5a6268',
                confirmButtonText: btnAgree,
                cancelButtonText: btnDisagree,
            })
        }

        static showDeleteDialog() {
            return Swal.fire({
                icon: `warning`,
                text: deleteGroup,
                showCancelButton: true,
                confirmButtonColor: '#C21010',
                cancelButtonColor: '#5a6268',
                confirmButtonText: btnAgree,
                cancelButtonText: btnDisagree,
            })
        }

        static showTimeOut(title, text, time, action) {
            let timerInterval;
            return Swal.fire({
                title: title,
                html: `${text}<br>in <b></b> seconds.`,
                timer: time,
                timerProgressBar: true,
                didOpen: () => {
                    Swal.showLoading();
                    const b = Swal.getHtmlContainer().querySelector('b');
                    timerInterval = setInterval(() => {
                        b.textContent = Math.round(Swal.getTimerLeft() / 1000)
                    }, 1000)
                },
                willClose: () => {
                    clearInterval(timerInterval)
                }
            }).then((result) => {
                if (result.dismiss === Swal.DismissReason.timer) {
                    action();
                }
            })
        }
    }

    static IziToast = class {
        static showSuccessAlert(t) {
            iziToast.success({
                title: iziToastTitleSuccess,
                position: 'topRight',
                timeout: 2500,
                message: t
            });
        }

        static showErrorAlert(t) {
            iziToast.error({
                title: iziToastTitleError,
                position: 'topRight',
                timeout: 3500,
                message: t
            });
        }
    }

    static allowShowingPassword(form) {
        let password = form.find('.password').get(0);
        let checkbox = form.find('.show-password');

        password.type = 'password';

        checkbox.on('change', function () {
            if (checkbox.is(':checked')) {
                password.type = 'text';
            } else {
                password.type = 'password';
            }
        })
    }

    static handleSpinner() {
        let str = `<div class="spinner d-flex justify-content-center align-items-center">
                        <div class="d-flex flex-column align-items-center">
                            <img src="/assets/img/loading.gif">
                            <div class="spinner-caption text-primary font-weight-bold">
                                ${spinnerUpload}
                            </div>
                        </div>
                    </div>`
        $('body').append(str);

        let spinner = $('.spinner');
        $(document)
            .ajaxStart(function () {
                spinner.show();
            })
            .ajaxStop(function () {
                spinner.hide();
                spinner.remove();
            });
    }

    static loadingbar = class {
        static setToDBMode() {
            topbar.config({
                autoRun: true,
                barThickness: 5,
                barColors: {
                    "0": "rgba(26,  188, 156, .9)",
                    ".25": "rgba(52,  152, 219, .9)",
                    ".50": "rgba(241, 196, 15,  .9)",
                    ".75": "rgba(230, 126, 34,  .9)",
                    "1.0": "rgba(211, 84,  0,   .9)",
                },
                shadowBlur: 10,
                shadowColor: "rgba(0,   0,   0,   .6)",
                className: null,
            })
        }

        static setToLGMode() {
            topbar.config({
                autoRun: true,
                barThickness: 5,
                barColors: {
                    "0": "#820000",
                    ".3": "#FFB200",
                    ".7": "#FFCB42",
                    "1.0": "#FFF4CF",
                },
                shadowBlur: 10,
                shadowColor: "rgba(0,   0,   0,   .6)",
                className: null,
            })
        }

        static setToUploadMode() {
            topbar.config({
                autoRun: false,
                barThickness: 5,
                barColors: {
                    "0": "rgba(26,  188, 156, .9)",
                    ".25": "rgba(52,  152, 219, .9)",
                    ".50": "rgba(241, 196, 15,  .9)",
                    ".75": "rgba(230, 126, 34,  .9)",
                    "1.0": "rgba(211, 84,  0,   .9)",
                },
                shadowBlur: 10,
                shadowColor: "rgba(0,   0,   0,   .6)",
                className: null,
            })
        }

        static setProgress(rate) {
            topbar.progress(rate);
        }

        static start() {
            topbar.show();
        }

        static stop() {
            topbar.hide();
        }

        static handleLoadingBarDB() {
            this.setToDBMode();
            $(document)
                .ajaxStart(function () {
                    CommonApp.loadingbar.start();
                })
                .ajaxStop(function () {
                    CommonApp.loadingbar.stop();
                });
        }

        static handleLoadingBarLG() {
            this.setToLGMode();
            $(document)
                .ajaxStart(function () {
                    CommonApp.loadingbar.start();
                })
                .ajaxStop(function () {
                    CommonApp.loadingbar.stop();
                });
        }
    }

    static clipText(data, lengthShown) {
        if (data === null || data === '')
            return `<i class="fas fa-edit text-primary"></i>`;
        return (data.length > lengthShown) ? (data.substr(0, lengthShown) + "...") : data
    }

    static enableEnterKeyboard(form, btn) {
        form.keypress(function (event) {
            let keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                btn.click();
                return false;
            }
        });
    }

    static disableBrowserAlert() {
        window.alert = function (text) {
            console.log(text);
        };
    }
}
