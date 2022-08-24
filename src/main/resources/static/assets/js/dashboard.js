class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_USER = this.DOMAIN + "/api/users";
    static BASE_URL_USER_GROUPS = this.DOMAIN + "/api/users/asgnGrp";
    static BASE_URL_AMZN_ACCOUNT = this.DOMAIN + "/api/amzn-account";
    static BASE_URL_GROUP = this.DOMAIN + "/api/groups";
    static BASE_URL_FILE_UPLOAD = this.DOMAIN + "/api/file-upload";

    static ERROR_400 = "Task failed, please check your data.";
    static ERROR_401 = "Access timeout."
    static ERROR_403 = "Access denied. Unauthorized personnel cannot perform this action.";
    static ERROR_404 = "An error occurred. Please try again later!";
    static ERROR_409 = "Task failed, please check your data.";
    static ERROR_500 = "Server error. Please contact admin";
    static SUCCESS_CREATED = "Data created successfully!";
    static SUCCESS_UPDATED = "Data updated successfully!";

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

        static showChangeStatusDialog(t) {
            return Swal.fire({
                icon: `question`,
                text: `Are you sure to ${t ? 'activate' : 'disable'} this user?`,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#5a6268',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
            })
        }

        static showDeleteGroupDialog() {
            return Swal.fire({
                icon: `warning`,
                text: `Are you sure to DELETE this group?`,
                showCancelButton: true,
                confirmButtonColor: '#C21010',
                cancelButtonColor: '#5a6268',
                confirmButtonText: 'Yes',
                cancelButtonText: 'No',
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
                        b.textContent = Math.round(Swal.getTimerLeft()/1000)
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
                title: 'Success',
                position: 'topRight',
                timeout: 2500,
                message: t
            });
        }

        static showErrorAlert(t) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 3500,
                message: t
            });
        }
    }

    static formatNumber() {
        $(".num-space").number(true, 0, ',', ' ');
        $(".num-point").number(true, 0, ',', '.');
        $(".num-comma").number(true, 0, ',', ',');
    }

    static formatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "").replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }

    static removeFormatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "")
    }

    static formatTooltip() {
        $('[data-toggle="tooltip"]').tooltip();
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

    static handleFailedTasks(jqXHR) {
        switch (jqXHR.status) {
            case 400:
                this.IziToast.showErrorAlert(this.ERROR_400);
                break;
            case 401:
                this.SweetAlert.showTimeOut(
                    this.ERROR_401,
                    "Redirecting to login",
                    4000,
                    this.redirectToLogin(4000));
                break;
            case 403:
                this.SweetAlert.showErrorAlert(this.ERROR_403);
                break;
            case 404:
                this.IziToast.showErrorAlert(this.ERROR_404);
                break;
            case 409:
                this.IziToast.showErrorAlert(this.ERROR_409);
                break;
            default:
                this.IziToast.showErrorAlert(this.ERROR_500);
                break;
        }
    }

    static redirectToLogin(timeout) {
        setTimeout(() => {
            location.href = "/logout";
        }, timeout);
    }
}

class User {
    constructor(id, username, fullName, disabled) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.disabled = disabled;
    }
}

class Role{
    constructor(id, code, name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}

class Product {
    constructor(id, name, price, quantity, isLocked, category, createdBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isLocked = isLocked;
        this.category = category;
        this.createdBy = createdBy;
    }
}

class Category {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }
}