class DBApp {
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

    static handleFailedTasks(jqXHR) {
        switch (jqXHR.status) {
            case 400:
                CommonApp.IziToast.showErrorAlert(ERROR_400);
                break;
            case 401:
                window.alert = function () {};
                CommonApp.SweetAlert.showTimeOut(
                    ERROR_401,
                    warningRedirect,
                    4000,
                    this.redirectToLogin(4000));
                break;
            case 403:
                CommonApp.SweetAlert.showErrorAlert(ERROR_403);
                break;
            case 404:
                CommonApp.IziToast.showErrorAlert(ERROR_404);
                break;
            case 409:
                CommonApp.IziToast.showErrorAlert(jqXHR.responseJSON.message);
                break;
            default:
                CommonApp.IziToast.showErrorAlert(ERROR_500);
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
            "url": CommonApp.BASE_URL_GROUP + "/findAllGroups",
        })
            .done((groups) => {
                let str = ``

                $.each(groups, function (index, group) {
                    str += `<a class="collapse-item" data-id="${group.id}" href="/dashboard/${group.id}">${group.title}</a>`
                })

                $.when(container.append(str)).then(function () {
                    DBApp.handleStatusNavDashboard();
                    DBApp.handleCollapsedFilterSidebar();

                    DBApp.autoScrollFilterSidebar();
                });

                this.handleFilter('filter-sidebar',
                    'sidebar-grp-search',
                    'collapse-item');

            })
            .fail((jqXHR) => {
                CommonApp.IziToast.showErrorAlert(ERROR_GRP_RETRIEVE);
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

    static autoScrollFilterSidebar() {
        let position = $(`#filter-sidebar .active`).offset().top ;
        $('#filter-sidebar').scrollTop(position - 210);
    }

    static handleCollapsedFilterSidebar() {
        let container = $('#filter-sidebar');
        let collapsedField = $("#collapseDashboard");

        if (container.find(".active")[0])
            collapsedField.collapse("show");
    }

    static handleBtnFullScreen() {
        let btnFullScreen = $('#btn-full-screen');
        let icon = $('#btn-full-screen>i');

        btnFullScreen.on('click', function () {
            if (document.fullscreenElement) {
                document.exitFullscreen()
                    .then(() => {
                        icon.removeClass('fa-compress');
                        icon.addClass('fa-expand');
                    })
                    .catch((err) => console.error(err))
            } else {
                document.documentElement.requestFullscreen();
                icon.removeClass('fa-expand');
                icon.addClass('fa-compress');
            }
        })
    }

    static handleChangePasswordSidebar() {
        let modalCP = $('#mdCPSelf');
        let formCP = $('#frmCPSelf');
        let btnSavePassword = $('#btnCPSelf');
        let newPassword;

        CommonApp.allowShowingPassword(formCP);
        btnSavePassword.on('click', function () {
            newPassword = $('#passwordCPSelf').val();
            formCP.submit();
        })

        modalCP.on('hidden.bs.modal', function () {
            formCP[0].reset();
            CPSelfValidator.resetForm();
            $("#mdCPSelf input").removeClass("error");
        })

        const CPSelfValidator = formCP.validate({
            rules: {
                passwordCPSelf: {
                    required: true,
                    minlength: 5,
                    maxlength: 128
                }
            },
            messages: {
                passwordCPSelf: {
                    required: validEmpty,
                    minlength: validPassword,
                    maxlength: validPassword
                }
            },
            submitHandler: function () {
                DBApp.changePasswordSelf(newPassword);
            }
        });
    }

    static changePasswordSelf(newPassword) {
        return $.ajax({
            "headers": {
                "accept": "application/json",
                "content-type": "application/json"
            },
            "type": "PUT",
            "url": CommonApp.BASE_URL_USER + "/changePassword",
            "data": newPassword
        })
            .done(() => {
                $('#frmCPSelf')[0].reset();
                $('#mdCPSelf').modal("hide");

                CommonApp.SweetAlert.showSuccessAlert(SUCCESS_UPDATED);
            })
            .fail((jqXHR) => {
                DBApp.handleFailedTasks(jqXHR);
            })
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

class AmznUser {
    constructor(id, published, tier, slotRemaining, slotTotal, reject, remove, note) {
        this.id = id;
        this.published = published;
        this.tier = tier;
        this.slotRemaining = slotRemaining;
        this.slotTotal = slotTotal;
        this.reject = reject;
        this.remove = remove;
        this.note = note;
    }
}

(() => {
    DBApp.handleChangePasswordSidebar();
    CommonApp.loadingbar.handleLoadingBarDB();
    CommonApp.enableEnterKeyboard($('#mdCPSelf'), $('#btnCPSelf'));
})()