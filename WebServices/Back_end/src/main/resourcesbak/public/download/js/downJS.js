$(function () {
    //字体浮现
    $('#title').fadeIn(500, function () {
        $('#btns').fadeIn(500)
    });
    $('#info_msg').fadeIn(1000)



    $('#logo_img_box>a').on({
        'mouseenter': function () {
            $(this).css({
                color: 'rgb(255,255,255)'
            });
        },
        'mouseleave': function () {
            $(this).css({
                color: 'rgba(255,255,255,0.8)'
            });
        }
    });
    $('.btn').on({
        'mouseenter': function () {
            $(this).css({
                fontWeight: 'bold'
            }).siblings().css({
                fontWeight: 'normal'
            });
        },
        'mouseleave': function () {
            $(this).css({
                fontWeight: 'normal'
            })
        }
    });
    var Main = {
        methods: {
            win() {
                this.$message({
                    message: '下载请求发送成功！',
                    type: 'success'
                });
            }
        }
    }
    var Ctor = Vue.extend(Main)
    new Ctor().$mount('#down_web')
});