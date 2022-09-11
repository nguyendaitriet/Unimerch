class App {
    static DOMAIN = location.origin;
    static API_ENDPOINT = this.DOMAIN + "/api";
    static BASE_URL_AUTHORIZATION = `${this.API_ENDPOINT}/login`;
    static BASE_URL_DASHBOARD = this.DOMAIN + "/api/dashboard";
    static BASE_URL_ORDER = this.DOMAIN + "/api/orders";
    static BASE_URL_USER = this.DOMAIN + "/api/users";
    static BASE_URL_USER_GROUPS = this.DOMAIN + "/api/users/asgnGrp";
    static BASE_URL_AMZN_ACCOUNT = this.DOMAIN + "/api/amzn";
    static BASE_URL_GROUP = this.DOMAIN + "/api/groups";
    static BASE_URL_ORDER = this.DOMAIN + "/api/orders";
    static BASE_URL_PRODUCT = this.DOMAIN + "/api/products";
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

    static formatNumber() {
        $(".num-space").number(true, 0, ',', ' ');
        $(".num-point").number(true, 0, ',', '.');
        $(".num-comma").number(true, 0, ',', ',');
    }

    static fmtNumComma(x) {
        if (x == null)
            return x;

        return x.toString().replace(/ /g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }

    static fmtNumWMetricSuffix(x) {
        if (x >= 1000000)
            return this.fmtNumComma((x / 1000000).toFixed(1).replace(/\.0$/, '')) + 'M';
        if (x >= 1000)
            return this.fmtNumComma((x / 1000).toFixed(1).replace(/\.0$/, '')) + 'k';
        return x;
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
                this.IziToast.showErrorAlert(ERROR_400);
                break;
            case 401:
                this.SweetAlert.showTimeOut(
                    ERROR_401,
                    warningRedirect,
                    4000,
                    this.redirectToLogin(4000));
                break;
            case 403:
                this.SweetAlert.showErrorAlert(ERROR_403);
                break;
            case 404:
                this.IziToast.showErrorAlert(ERROR_404);
                break;
            case 409:
                this.IziToast.showErrorAlert(jqXHR.responseJSON.message);
                break;
            default:
                this.IziToast.showErrorAlert(ERROR_500);
                break;
        }
    }

    static redirectToLogin(timeout) {
        setTimeout(() => {
            location.href = "/logout";
        }, timeout);
    }

    static handleFilter(containerId, filterId, dataClassName) {
        let rawKeywords, keywords, groupName;
        let filter = $('#' + filterId);
        let rows = $('#' + containerId + ' .' + dataClassName);

        filter.on('input', () => {
            rawKeywords = filter.val();
            keywords = rawKeywords.toUpperCase();

            $.each(rows, function (index, row) {
                groupName = $(row).text();
                if (groupName.toUpperCase().includes(keywords)) {
                    $(row).css('display', '');
                } else {
                    $(row).css('display', 'none');
                }
            })
        })
    }

    static handleGroupsFilterSidebar() {
        let container = $('#filter-sidebar');

        $.ajax({
            "headers": {
                "accept": "application/json",
                "content-type": "application/json"
            },
            "type": "GET",
            "url": App.BASE_URL_GROUP + "/findAllGroups",
        })
            .done((groups) => {
                let str = ``

                $.each(groups, function (index, group) {
                    str += `<a class="collapse-item" data-id="${group.id}" href="/dashboard/${group.id}">${group.title}</a>`
                })
                container.append(str);

                this.handleFilter('filter-sidebar', 'sidebar-grp-search', 'collapse-item');

                this.handleStatusNavDashboard();
                this.handleCollapsedFilterSidebar();
            })
            .fail((jqXHR) => {
                App.IziToast.showErrorAlert(ERROR_GRP_RETRIEVE);
            })
    }

    static handleStatusNavDashboard() {
        let currentURL = window.location.pathname.split('/');
        let domain = currentURL[1];
        let id = currentURL[2];
        let navDashboard = $("#nav-item-dashboard");
        let rows = $("#filter-sidebar .collapse-item");

        if (id === '')
            id = undefined;

        if ((domain === 'dashboard' || domain === 'users') && ((Number(id)) || id === undefined)) {
            navDashboard.addClass("active");
        }

        $.each(rows, function (index, row) {
            if (domain == 'users' && id === undefined) {
                $(row).addClass('active');
                return false;
            }
            if ($(row).data('id') == id) {
                $(row).addClass('active');
                return false;
            }
        })
    }

    static handleCollapsedFilterSidebar() {
        let container = $('#filter-sidebar');
        let collapsedField = $("#collapseDashboard");

        if (container.find(".active")[0])
            collapsedField.collapse("show");
    }

    static handleSpinner() {
        let str = `<div id="spinner"></div>`;
        $('body').append(str);

        let spinner = $('#spinner');
        $(document)
            .ajaxStart(function () {
                spinner.show();
            })
            .ajaxStop(function () {
                spinner.hide();
            });
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

class Group {
    constructor(id, title) {
        this.id = id;
        this.title = title;
    }
}


(() => {
    App.handleSpinner();
})()