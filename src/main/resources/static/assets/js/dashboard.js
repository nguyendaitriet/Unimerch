class App {
    static DOMAIN = location.origin;

    static BASE_URL_AUTHORIZATION = this.DOMAIN + "/api/auth";
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_USER = this.DOMAIN + "/api/users";
    static BASE_URL_AMZN_ACCOUNT = this.DOMAIN + "/api/amzn-account";
    static BASE_URL_GROUP = this.DOMAIN + "/api/groups";
    static BASE_URL_FILE_UPLOAD = this.DOMAIN + "/api/file-upload";

    static ERROR_400 = "Task failed, please check your data.";
    static ERROR_401 = "Access timeout. Redirecting to login."
    static ERROR_403 = "Access denied. Unauthorized personnel cannot perform this action.";
    static ERROR_404 = "An error occurred. Please try again later!";
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
}

class User {
    constructor(id, username, fullName, password, disabled) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.disabled = disabled;
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