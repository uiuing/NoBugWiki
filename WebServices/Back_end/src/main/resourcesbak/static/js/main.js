
// 调整左侧菜单栏收放
var RightSideMenu_flage = false;
var time_set;
function RightSideMenu() {
    if (RightSideMenu_flage) { // 关闭
        window.clearTimeout(time_set);
        $(".el-tabs__nav-wrap").eq(0).animate({ left: '-80px' }, 100);
        $("#more").animate({ left: '7px' }, 90);
        $(".el-tabs__nav-wrap").eq(0).animate({ width: '0px' }, 100);
    } else { // 打开
        $(".el-tabs__nav-wrap").eq(0).animate({ width: '100px' }, 90);
        $(".el-tabs__nav-wrap").eq(0).animate({ left: '0px' }, 100);
        $("#more").animate({ left: '106px' }, 100);
        // 设置定时器
        time_set = window.setTimeout(function () {
            RightSideMenu();
        }, 3500);
        // 检测点击菜单时间
        $(".el-tabs__nav-scroll").click(function () {
            //去掉定时器的方法  
            window.clearTimeout(time_set);
            time_set = window.setTimeout(function () {
                RightSideMenu();
            }, 3500);
        });
    }
    RightSideMenu_flage = !RightSideMenu_flage;
}

// 用户信息
var userinfo;
// 用户贡献数据
var user_Frequency;
// 热门flage
var hot_flage = true;


// 初始化用户数据
function on() {

    // 检测用户登录情况
    var obj = JSON.parse(localStorage.getItem("user"));
    if (obj && Inspect_time(obj.time)) {
        ImplantationStatus(obj);
    } else {
        var obj = JSON.parse(sessionStorage.getItem("user"));
        if (obj) {
            ImplantationStatus(obj);
        } else {
            // 开启登录按钮
            document.getElementById("login-btn").style.display = "block";
            document.getElementById("login-win").style.display = "none";
            document.getElementById("todo_tips").style.display = "none";
            document.getElementsByClassName("el-badge__content--undefined")[0].style.display = "none"
            // 未登录关闭用户信息
            document.getElementById("tab-1").style.display = "none";
            // 未登录关闭热门推荐
            document.getElementById("tab-2").style.display = "none";
        }
    }

    let tips = [
        "在这里输入您在开发中遇到的麻烦",
        "Nobug会尽力为您寻找解决方案",
        "例如:  \"jvm内存优化\"",
    ];

    //  打字机效果
    let check_input = setInterval(function () {
        if (Home_Page.input !== "") {
            $(".input_tips").animate({ marginTop: '-65px' }, 300);
            clearInterval(check_input);
        }
    }, 250)
    if (Home_Page.input !== "") {
        $(".input_tips").animate({ marginTop: '-65px' }, 300);
    }
    let SpaceTime = 0;
    let time_RAM = 0;
    for (let i = 0; i < tips.length; i++) {
        let time = (tips[i].length - 1) * 2 * 90 * i;
        if (i !== 0) {
            time_RAM += time;
        }
        setTimeout(function () {
            setTimeout(function () {
                if (i !== 2) {
                    remove_text(tips[i], 60);
                } else {
                    $(".input_tips").animate({ marginTop: '-65px' }, 300);
                    $(".input_tips").fadeOut(500);
                }
            }, add_text(tips[i], 90) + 1200);
        }, time + time_RAM);
    }
}

// 打字机效果 for 新增
function add_text(tips, time) {
    for (let i = 0; i < tips.length; i++) {
        setTimeout(function () {
            Home_Page.input_tips += tips[i];

        }, i * time)
    }
    return (tips.length - 1) * time;
}
// 打字机效果 for 删除
function remove_text(tips, time) {
    for (let i = tips.length; i >= 0; i--) {
        setTimeout(function () {
            Home_Page.input_tips = tips.slice(0, i);
        }, (tips.length - i + 1) * time)
    }
}

// 判断有没有过期
function Inspect_time(time) {
    if (new Date().getTime() / 1000 / 60 / 60 / 24 - time / 1000 / 60 / 60 / 24 < 15) {
        return true;
    }
    return false;
}
// 产生用户信息
function user_init_info(obj) {
    document.getElementById("user_name").innerHTML = obj.name;
}
/**
 * @description  : 初始状态
 * @RelyOn：on（） 
 * @param         {*} obj
 * @return        {*}
 */
async function ImplantationStatus(obj) {
    userinfo = obj;
    // 用户贡献图 & 领域分析内容
    if (document.getElementsByClassName("cal-heatmap-container")) {
        for (let i = 0; i < (document.getElementsByClassName("cal-heatmap-container").length); i++) {
            document.getElementsByClassName("cal-heatmap-container")[i].remove();
        }
    }
    Set_user_Frequency();
    // 开启用户信息
    document.getElementById("tab-1").style.display = "block";
    // 开启热门推荐
    document.getElementById("tab-2").style.display = "block";
    // 关闭登录按钮
    document.getElementById("login-btn").style.display = "none";
    document.getElementById("login-win").style.display = "block";
    document.getElementById("todo_tips").style.display = "block";
    document.getElementsByClassName("el-badge__content--undefined")[0].style.display = "block"
    // 音乐歌单
    if (document.getElementById("music").innerHTML === "") {
        document.getElementById("music").innerHTML = ' <meting-js id="' + localStorage.getItem("musicid") + '"order="random" server="netease" loop="all"order="list"type="playlist"fixed="true"list-olded="true" </meting-js>';
    }
    // 加载热门内容
    setTimeout(function () {
        $('#tab-2').click(function (e) {
            if (hot_flage) {
                new_hot();
                hot_flage = false;
            }
        });

    }, 300);
    // 用户信息管理加载
    user_init_info(obj);
    // 生成词云
    new_ci_yun(userinfo.email);
}
/**
* @description  : 生成热门内容
* @RelyOn：on（）
* @param         {*} 
* @return        {*}
*/
function new_hot() {
    setTimeout(function () {
        document.getElementById("pane-2").innerHTML = '<iframe id="PG-' + Home_Page.LeftSideMenuTabsValue + '" src="hot.html" width="100%" frameBorder="0" height="' + window.screen.availHeight + 'px" ></iframe>';
    }, 1);
}
/**
* @description  : 生成词云
* @RelyOn：on（）
* @param         {*}
* @return        {*}
*/
async function new_ci_yun(email) {
    const res = await axios.post('/user/get/direction_key', {
        email: email,
    });
    let obj = res.data;
    let index = true;
    let JosnList;
    for (let key in obj) {
        if (index) {
            JosnList = [{ name: key.toString(), value: obj[key] }];
            index = false;
        } else {
            JosnList.push({ name: key.toString(), value: obj[key] });
        }
    }
    var optionFour = {
        tooltip: {
            show: true
        },
        series: [{
            type: 'wordCloud',
            sizeRange: [10, 50],//文字范围
            //文本旋转范围，文本将通过rotationStep45在[-90,90]范围内随机旋转
            rotationRange: [-45, 90],
            rotationStep: 45,
            textRotation: [0, 45, 90, -45],
            //形状
            shape: 'circle',
            textStyle: {
                normal: {
                    color: function () {//文字颜色的随机色
                        return 'rgb(' + [
                            Math.round(Math.random() * 250),
                            Math.round(Math.random() * 250),
                            Math.round(Math.random() * 250)
                        ].join(',') + ')';
                    }
                },
                //悬停上去的字体的阴影设置
                emphasis: {
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            data: JosnList
        }]
    };
    var myChartFour = echarts.init(document.getElementById('ci_yun'));
    //使用制定的配置项和数据显示图表
    myChartFour.setOption(optionFour);

}

// 懒加载
window.onload = function () {
    document.body.style.display = 'block';
    // 设置默认输入
    document.getElementById("search").focus();
    // 收起左侧菜单
    $("#more").css({ left: '10px' });
    $(".el-tabs__nav-wrap").eq(0).css({ left: '-80px' });
    $(".el-tabs__nav-wrap").eq(0).css({ width: '0px' });
    // 初始化数据
    on();
};


/**
 * @description  : 获取用户贡献数据 & 生成领域图
 * @param         {*}
 * @return        {*}
 */
async function Set_user_Frequency() {
    if (user_Frequency) {
        add_Frequency(user_Frequency);
    } else {
        let obj = sessionStorage.getItem('user_frequency');
        if (obj) {
            user_Frequency = obj;
            add_Frequency(obj);
        } else {
            if (userinfo) {
                // 延迟加载用户信息
                setTimeout(async function () {
                    const res = await axios.post('/user/get/value', {
                        email: userinfo.email,
                    });
                    if (res.data) {
                        let value = res.data;
                        user_Frequency = obj;
                        // 存入缓存
                        sessionStorage.setItem("user_Frequency",
                            value
                        );
                        // 添加
                        add_Frequency(res.data);
                    }
                }, 2000);
                // 生成用户领域图
                setTimeout(async function () {
                    let ress = await axios.post('/user/get/direction', {
                        email: userinfo.email,
                    });
                    generate_domain_diagram(ress.data[0])
                }, 1900)
            }
        }
    }
}

function generate_domain_diagram(dataS) {

    var chartDom = document.getElementById('generate_domain_diagram');
    var myChart = echarts.init(chartDom);
    var option;
    let data =
        option = {
            tooltip: {
                trigger: 'item'
            },
            legend: {
                padding: [
                    37,  // 上
                    0, // 右
                    0,  // 下
                    0, // 左
                ]
            },
            series: [
                {
                    name: '领域',
                    type: 'pie',
                    radius: '50%',
                    data: [
                        { value: dataS.backend, name: '后端开发' },
                        { value: dataS.linuxom, name: 'Linux运维' },
                        { value: dataS.cloudbigdata, name: '云计算与大数据' },
                        { value: dataS.database_d, name: '数据库开发' },
                        { value: dataS.netsecurity, name: '信息安全' },
                        { value: dataS.frontend, name: 'WEB前端' },
                        { value: dataS.computermajor, name: '计算机专业知识' },
                        { value: dataS.ai, name: '人工智能' }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

    option && myChart.setOption(option);
    setTimeout(function () {
        document.getElementById("generate_domain_diagram").style.height = "511px";
    }, 1)
}

/**
 * @description  : 添加用户贡献图 && 刷新数据
 * @param         {*} user_Frequency
 * @return        {*}
 */
function add_Frequency(user_Frequency) {
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
    var cal = new CalHeatMap();
    cal.init({
        domain: "year",
        subDomain: "day",
        cellRadius: 3,
        legendCellSize: 8,
        previousSelector: "#previousSelector-a-previous",
        nextSelector: "#previousSelector-a-next",
        tooltip: true,
        onTooltip: function (date, nb) {
            return $filter('currency')(nb || 0, '€ ', 2);
        },
        rowLimit: 10,
        range: 1,
        cellSize: 9,
        legendHorizontalPosition: "right",
        label: {
            width: 27,
            position: "right",
            offset: { x: -10, y: 107 }
        },
        subDomainTitleFormat: {
            empty: '休养生息<br/>{date}',
            filled: "{count} {name} {date}"
        },
        domainLabelFormat: "%Y年",
        itemName: ["个贡献<br/>", "个贡献<br/>"],
        start: new Date(),
        subDomainDateFormat: "%Y年/%m月/%d日",
        data: user_Frequency
    });
}

function KeyWords() {
    return Home_Page.input;
}
function User_emails() {
    return userinfo.email;
}

/**
 * @description  : 生产信息 
 * @param         {*}
 * @return        {*}
 */
async function Production_Information() {
    // 新增标签
    Home_Page.addTab(Home_Page.LeftSideMenuTabsValue);
    setTimeout(function () {
        Home_Page.loading_body = true;

        document.getElementById("pane-" + Home_Page.LeftSideMenuTabsValue).innerHTML = '<iframe id="PG-' + Home_Page.LeftSideMenuTabsValue + '" src="cache.html" width="100%" frameBorder="0" height="' + window.screen.availHeight + 'px" ></iframe>';
        // 开启侧边栏
        RightSideMenu_flage = false;
        document.getElementById("PG-" + Home_Page.LeftSideMenuTabsValue).onload = function () {
            Home_Page.loading_body = false;
        }
    }, 1);
    RightSideMenu();
}

var Home_Page = new Vue({

    el: '#all',

    data() {

        // 注册用户名检查
        var upvalidateUser1 = async (rule, value, callback) => {
            if (value === '') {
                this.user_ck = false;
                this.upshow_ck();
                callback(new Error('请输入用户名'));
            } else {
                if (value) {
                    const res = await axios.post('/user/select/name', {
                        name: this.ruleForm.user1
                    });
                    if (!res.data) {
                        this.user_ck = true;
                        this.upshow_ck();
                        callback();
                    } else {
                        this.user_ck = false;
                        this.upshow_ck();
                        return callback(new Error('该用户名已经被注册'));
                    }
                }
            }
        };
        // 注册邮箱检查
        var upvalidatePass1 = async (rule, value, callback) => {
            if (value === '') {
                this.email_ck = false;
                this.upshow_ck();
                callback(new Error('邮箱号不能为空'));
            } else {
                const reg = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
                if ((reg.test(value))) {
                    const res = await axios.post('/user/select/email', {
                        email: value
                    });
                    if (!res.data) {
                        this.yzmshow = true;
                        this.ruleForm.email = value;
                        this.email_ck = true;
                        this.upshow_ck();
                        callback();
                    } else {
                        this.email_ck = false;
                        this.upshow_ck();
                        return callback(new Error('该邮箱已经被注册'));
                    }
                } else {
                    this.email_ck = false;
                    this.upshow_ck();
                    callback(new Error('请输入正确的邮箱'));
                }
            }
        };
        // 注册邮箱验证码验证
        var upvalidateCode = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入验证码'));
                this.code_ck = false;
                this.upshow_ck();
            } else {
                if (value == this.ruleForm.emailCode) {
                    this.code_ck = true;
                    this.upshow_ck();
                    callback();
                } else {
                    this.code_ck = false;
                    this.upshow_ck();
                    callback(new Error('验证码不正确'));
                }
            }
        };
        // 注册密码检查
        var upvalidatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
            }
        };
        // 注册双重密码检查
        var upvalidatePass2 = (rule, value, callback) => {
            if (value === '') {
                this.pass2_ck = false;
                this.upshow_ck();
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.pass) {
                this.pass2_ck = false;
                this.upshow_ck();
                callback(new Error('两次输入密码不一致!'))
            } else {
                this.pass2_ck = true;
                this.upshow_ck();
                callback();
            }
        };
        // 登录邮箱检查
        var invalidateUser = (rule, value, callback) => {
            if (value === '') {
                this.pass1_ck = false;
                this.inshow_ck();
                callback(new Error('邮箱不能为空'));
            } else {
                const reg = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
                if ((reg.test(value))) {
                    this.pass1_ck = true;
                    this.inshow_ck();
                    callback();
                } else {
                    this.pass1_ck = false;
                    this.inshow_ck();
                    callback(new Error('请输入正确的邮箱'));
                }
            }
        };
        // 登录密码检查
        var invalidatePass = (rule, value, callback) => {
            if (value === '') {
                this.user1_ck = false;
                this.inshow_ck();
                callback(new Error('请输入密码'));
            } else {
                this.user1_ck = true;
                this.inshow_ck();
                callback();
            }
        };

        return {
            // 刷新内容缓存效果
            refresh_data_flage: false,

            // 加载缓冲
            loading_body: false,

            // 查询结果
            RAMTabsValue: '1',
            RAMTabs: [],

            LeftSideMenuTabs: [],
            // 检索结构与标签处理
            LeftSideMenuTabsValue: '0',
            LeftSideMenuIndex: 0,

            //显示模式的按钮
            PreviewAndView: true,

            // 登录提示
            logo_logo: true,

            // 登录加载提示
            LogoLoading: false,

            // 自动登录开关
            auto_log: true,

            // 验证码验证开关
            fullscreenLoading: false,

            // 登录功能开关
            inshow: true,
            // 注册功能开关
            upshow: true,
            // 注册输入检查
            user_ck: false,
            email_ck: false,
            pass2_ck: false,
            code_ck: false,
            // 登录输入检查
            user1_ck: false,
            pass1_ck: false,

            // 登录注册功能开关
            show: true,

            // 验证码倒计时
            count: '',
            // 验证码事件
            timer: null,
            // 获取验证码开关
            yzmshow: false,

            // 用户输入信息
            ruleForm: {
                email: '',
                user1: '',
                pass1: '',
                pass: '',
                checkPass: '',
                code: ''
            },

            // 验证码
            loginForm: {
                code: '00',
            },

            // 登录信息
            urlQuery: '',
            ruleForm: {
                pass: '',
                user: ''
            },

            // 登录与注册的检查开始
            rules: {
                code: [{
                    required: true,
                    validator: upvalidateCode,
                    trigger: 'blur'
                },
                {
                    min: 4,
                    message: '长度为6',
                    trigger: 'blur'
                }
                ],
                user1: [{
                    required: true,
                    validator: upvalidateUser1,
                    trigger: 'blur'
                }],
                pass1: [{
                    required: true,
                    validator: upvalidatePass1,
                    trigger: 'blur'
                }],
                // 密码
                pass: [{
                    required: true,
                    validator: upvalidatePass,
                    trigger: 'blur'
                }
                ],
                // 校验密码
                checkPass: [{
                    required: true,
                    validator: upvalidatePass2,
                    trigger: 'blur'
                }
                ],
                user: [{ required: true, validator: invalidateUser, trigger: 'blur' }],
                pass: [{ required: true, validator: invalidatePass, trigger: 'blur' }]
            },

            // 登录注册切换
            activeName: 'first',
            // 登录&注册框
            Login_flage: false,
            // 搜索框
            input: '',
            // 输入提示
            input_tips: '',
            //  退出flage
            visible: false,
            // 设置音乐id
            input_musicid: '',
            // 用户设置的内容加载速度
            speeds: 50,
            marks: {
                10: {
                    style: {
                        color: '#00DD00'
                    },
                    label: this.$createElement('strong', '高异步')
                },
                50: {
                    style: {
                        color: '#1989FA'
                    },
                    label: this.$createElement('strong', '默认')
                },
                90: {
                    style: {
                        color: '#FF0000 '
                    },
                    label: this.$createElement('strong', '高并发')
                }
            }
        }
    },

    methods: {
        // 刷新数据内容
        refresh_data() {
            this.refresh_data_flage = true;
            this.$notify({
                title: '刷新成功',
                message: '按钮已锁定,数据更新时会自动释放按钮',
                type: 'success'
            });
            on();
        },
        // 提交音乐id
        update_musicid() {
            if (this.input_musicid !== "" && parseInt(this.input_musicid)) {
                this.set_musicId(parseInt(this.input_musicid));
                this.$notify({
                    title: '成功',
                    message: '设置成功！',
                    type: 'success'
                });
                setTimeout(function () {
                    Home_Page.$notify({
                        title: '警告',
                        message: '如果歌单ID不正确 则会自动关闭音乐功能',
                        type: 'warning'
                    });
                }, 2500);
            } else {
                this.$notify.error({
                    title: '错误',
                    message: '格式不对哦~',
                });
            }
        },
        // 设置音乐id
        async set_musicId(id) {
            const res = await axios.post('/user/set/value', {
                email: userinfo.email,
                musicid: id,
            });
            localStorage.setItem("musicid", id);
            document.getElementById("music").innerHTML = ' <meting-js id="' + id + '"order="random" server="netease" loop="all"order="list"type="playlist"fixed="true"list-olded="true" </meting-js>';
        },
        // 退出登录
        logout() {
            localStorage.clear();
            sessionStorage.clear();
            setTimeout(function () {
                location.replace(window.location.href);
            }, 100);
        },
        // 移除菜单选项
        LeftSideMenuTabs_removeTab(targetName) {
            let tabs = this.LeftSideMenuTabs;
            let activeName = this.LeftSideMenuTabsValue;
            if (activeName === targetName) {
                tabs.forEach((tab, index) => {
                    if (tab.name === targetName) {
                        let nextTab = tabs[index + 1] || tabs[index - 1];
                        if (nextTab) {
                            activeName = nextTab.name;
                        }
                    }
                });
            }
            this.LeftSideMenuTabsValue = activeName;
            this.LeftSideMenuTabs = tabs.filter(tab => tab.name !== targetName);
        },

        // 添加菜单内容
        addTab(targetName) {
            let newTabName = ++this.LeftSideMenuIndex + 2 + '';
            let h = new Date().getHours().toLocaleString('chinese', { hour12: false });
            let m = new Date().getMinutes();
            if (m <= 9) { m = "0" + m; }
            this.LeftSideMenuTabs.push({
                title: h + ":" + m + ":" + new Date().getSeconds(),
                name: newTabName,
                content: 'New Tab content'
            });
            this.LeftSideMenuTabsValue = newTabName;
        },
        handleTabsEdit(targetName, action) {
            if (action === 'remove') {
                let tabs = this.LeftSideMenuTabs;
                let activeName = this.LeftSideMenuTabsValue;
                if (activeName === targetName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === targetName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                activeName = nextTab.name;
                            }
                        }
                    });
                }

                this.LeftSideMenuTabsValue = activeName;
                this.LeftSideMenuTabs = tabs.filter(tab => tab.name !== targetName);
            }
        },
        // 输入框回车事件
        async Confirm_Input() {
            if (userinfo) {
                // 去空
                if (this.input !== '') {
                    Production_Information();
                    await axios.post('/user/set/value', {
                        email: userinfo.email
                    });
                    this.input === '';
                    document.getElementById("search").value = "";
                    this.refresh_data_flage = false;
                } else {
                    this.$message({
                        showClose: true,
                        message: '请输入内容再查询哦～～',
                        type: 'warning'
                    });
                }
            }
            else {
                this.$confirm('请先登录再使用', '提示', {
                    confirmButtonText: '前往',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.Login_flage = true;
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '没有账户可以免费注册哦～'
                    });
                });
            }
        },

        // 登录按钮事件
        async SignInCheckAndVerify() {
            if (!this.inshow) {
                this.LogoLoading = true;
                const res = await axios.post('/user/select/email', {
                    email: this.ruleForm.user
                });
                if (res.data) {
                    const res = await axios.post('/user/select/password', {
                        email: this.ruleForm.user,
                        password: this.ruleForm.pass,
                    });
                    if (res.data) {
                        const res = await axios.post('/user/select/all', {
                            email: this.ruleForm.user,
                        });
                        if (res.data[0].email) {
                            // 判断是否自动登录
                            if (this.auto_log) {
                                // 存入浏览器缓存
                                localStorage.setItem("user", JSON.stringify({
                                    "email": res.data[0].email,
                                    "name": res.data[0].name,
                                    "time": new Date().getTime()
                                }));
                            } else {
                                sessionStorage.setItem("user", JSON.stringify({
                                    "email": res.data[0].email,
                                    "name": res.data[0].name,
                                    "time": new Date().getTime()
                                }));
                            }
                            localStorage.setItem("musicid", 6586243036);
                            this.Login_flage = false;
                            document.getElementsByClassName("el-dialog__wrapper")[0].remove();
                            document.getElementsByClassName("v-modal")[0].remove();
                            on();
                            this.LogoLoading = false;
                            this.$message({
                                message: '欢迎使用NoBug',
                                type: 'success'
                            });
                        } else {
                            this.Login_flage = false;
                            this.LogoLoading = false;
                            this.$message.error('密码或邮箱错误！');
                        }
                    } else {
                        this.Login_flage = false;
                        this.LogoLoading = false;
                        this.$message.error('密码或邮箱错误！');
                    }
                } else {
                    this.Login_flage = false;
                    this.LogoLoading = false;
                    this.$message.error('该邮箱未注册哦！');
                }
            }
        },
        // 注册按钮事件
        async SignUpCheckAndVerify() {
            if (!this.upshow) {
                this.LogoLoading = true;
                await axios.post('/user/inster', {
                    email: this.ruleForm.email,
                    name: this.ruleForm.user1,
                    password: this.ruleForm.pass
                });
                await axios.post('/user/set/value', {
                    email: this.ruleForm.email,
                });
                // 判断是否自动登录
                if (this.auto_log) {
                    const res = await axios.post('/user/set/music', {
                        email: this.ruleForm.email,
                        musicid: 6586243036
                    });
                    // 存入浏览器缓存
                    localStorage.setItem("user", JSON.stringify({
                        "email": this.ruleForm.email,
                        "name": this.ruleForm.user1,
                        "time": new Date().getTime()
                    }));
                } else {
                    sessionStorage.setItem("user", JSON.stringify({
                        "email": this.ruleForm.email,
                        "name": this.ruleForm.user1,
                        "time": new Date().getTime()
                    }));
                }
                localStorage.setItem("musicid", 6586243036);
                this.Login_flage = false;
                document.getElementsByClassName("el-dialog__wrapper")[0].remove();
                document.getElementsByClassName("v-modal")[0].remove();
                on();
                this.LogoLoading = false;
                this.$message({
                    message: '欢迎使用NoBug',
                    type: 'success'
                });
            }
        },
        // 检查登录按钮
        inshow_ck() {
            if (this.user1_ck && this.pass1_ck) {
                this.inshow = false;
            } else {
                this.inshow = true;
            }
        },
        // 检查注册按钮
        upshow_ck() {
            if (this.user_ck && this.pass2_ck && this.email_ck && this.code_ck) {
                this.upshow = false;
            } else {
                this.upshow = true;
            }
        },
        // 获取验证码
        async getCode() {
            this.fullscreenLoading = true;
            const res = await axios.post('/mail/getCheckCode', {
                email: this.ruleForm.email
            });

            if (res.data) {
                this.fullscreenLoading = false;
                this.ruleForm.emailCode = res.data;
                this.$message({
                    message: '验证码发送成功',
                    type: 'success'
                });
            }
            const TIME_COUNT = 60;
            if (!this.timer) {
                this.count = TIME_COUNT;
                this.show = false;
                this.timer = setInterval(() => {
                    if (this.count > 0 && this.count <= TIME_COUNT) {
                        this.count--;
                    } else {
                        this.show = true;
                        clearInterval(this.timer);
                        this.timer = null;
                    }
                }, 1000)
            }
        }
    }
});