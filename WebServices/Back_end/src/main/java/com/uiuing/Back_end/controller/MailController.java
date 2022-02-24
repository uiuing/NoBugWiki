/*
 * @Author        : uiuing
 * @Date          : 2021-06-13 15:25:30
 * @LastEditTime  : 2021-06-27 19:27:41
 * @LastEditors   : uiuing
 * @Description   : 
 * @FilePath      : /Back_end/src/main/java/com/uiuing/Back_end/controller/MailController.java
 * ©️ uiuing.com
 */
package com.uiuing.Back_end.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.uiuing.Back_end.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
public class MailController {

    @Autowired
    JavaMailSenderImpl mailSender;

    @RequestMapping(value = "/mail/getCheckCode", method = RequestMethod.POST)
    public String getCheckCode(@RequestBody user users) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        String code = String.valueOf((int) ((Math.random()) * 1000000));
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject("Nobug账号--邮箱验证");
            helper.setText(getTemplate(code), true);
            helper.setTo(users.getEmail());
            helper.setFrom("cooluiu@qq.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return code;
    }
    

    private String getTemplate(String code) {
        String str = "";
        str += "<div class=\"mailwrapper\"";
        str += "    style=\"background-color:rgb(255, 255, 255);font-size: 14px; color: #333; font-smooth: always; -webkit-font-smoothing: antialiased; \">";
        str += "";
        str += "    <table style=\"table-layout: fixed;width: 100%;\">";
        str += "        <tbody>";
        str += "            <tr>";
        str += "                <td>";
        str += "";
        str += "                    <table class=\"mainTable\" align=\"center\"";
        str += "                        style=\"margin: 0 auto;font-size: inherit; line-height: inherit; text-align: center; border-spacing: 0; border-collapse: collapse; -premailer-cellpadding: 0; -premailer-cellspacing: 0; padding: 0; border: 0;\"";
        str += "                        cellpadding=\"0\" cellspacing=\"0\">";
        str += "                        <tbody>";
        str += "                            <tr>";
        str += "                                <td class=\"topPadding\"";
        str += "                                    style=\"font-family: 'Lucida Grande', Helvetica, Arial, sans-serif; height: 16px; -premailer-height: 16;\"";
        str += "                                    height=\"16\"></td>";
        str += "                            </tr>";
        str += "                            <tr>";
        str += "                                <td class=\"centerColumn\" style=\"width: 685px; -premailer-width: 685;\">";
        str += "                                    <table class=\"iPhone_font\"";
        str += "                                        style=\"font-family: 'Lucida Grande', Helvetica, Arial, sans-serif; font-size: inherit; line-height: 18px; padding: 0px; border: 0px;\">";
        str += "                                        <tbody>";
        str += "                                            <tr>";
        str += "                                                <td class=\"content_margin\" style=\"width: 40px;\"></td>";
        str += "                                                <td class=\"heading_logo\" style=\"                                 text-align: right;";
        str += "                 width: 600px;\"><img width=\"22\" height=\"26\"";
        str += "                                                        src=\"https://pic.imgdb.cn/item/60c00b0e844ef46bb23c42ee.png\"";
        str += "                                                        style=\"width: 22px; height: 26px;\"></td>";
        str += "                                                <td class=\"content_margin\" style=\"width: 40px;\"></td>";
        str += "                                            </tr>";
        str += "                                            <div style=\"background:#fff;border:1px solid #ccc;margin:2%;padding:0 30px\">";
        str += "                                                <div style=\"line-height:40px;height:40px\">&nbsp;</div>";
        str += "                                                <p";
        str += "                                                    style=\"margin:0;padding:0;font-size:14px;line-height:30px;color:#333;font-family:arial,sans-serif;font-weight:bold\">";
        str += "                                                    亲爱的用户：</p>";
        str += "                                                <div style=\"line-height:20px;height:20px\">&nbsp;</div>";
        str += "                                                <p";
        str += "                                                    style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'宋体',arial,sans-serif\">";
        str += "                                                    您好！感谢您使用NoBug，本次请求的验证码为：</p>";
        str += "                                                <p";
        str += "                                                    style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'宋体',arial,sans-serif\">";
        str += "                                                    <b style=\"font-size:18px;color:#f90\">";
        str += code;
        str += "                                                    </b><span";
        str += "                                                        style=\"margin:0;padding:0;margin-left:10px;line-height:30px;font-size:14px;color:#979797;font-family:'宋体',arial,sans-serif\">(为了保障您帐号的安全性，请在1小时内完成验证。)</span>";
        str += "                                                </p>";
        str += "                                                <div style=\"line-height:50px;height:80px\">&nbsp;</div>";
        str += "                                                <p";
        str += "                                                    style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'宋体',arial,sans-serif\">";
        str += "                                                    NoBug团队</p>";
        str += "                                                <p";
        str += "                                                    style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'宋体',arial,sans-serif\">";
        str += (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        str += "                                                </p>";
        str += "                                                <div style=\"line-height:20px;height:20px\">&nbsp;</div>";
        str += "                                            ";
        str += "                                            </div>";
        str += "                                            <tr class=\"footerTopPadding iPhone_font\"";
        str += "                                                style=\"height: 17px; -premailer-height: 17;\" height=\"17\">";
        str += "                                                <td style=\"font-family: 'Geneva', Helvetica, Arial, sans-serif;\"></td>";
        str += "                                            </tr>";
        str += "                                            <tr>";
        str += "                                                <td class=\"footer background iPhone_font\" colspan=\"3\"";
        str += "                                                    background=\"https://statici.icloud.com/emailimages/v4/common/footer_gradient_web.png\"";
        str += "                                                    style=\"font-family: 'Geneva', Helvetica, Arial, sans-serif; font-smooth: always; -webkit-font-smoothing: antialiased; width: 685px; font-size: 11px; line-height: 14px; color: #888; text-align: center; background-repeat: no-repeat; background-position: center top; padding: 15px 0 12px; -webkit-text-size-adjust:100%;\"";
        str += "                                                    align=\"center\">";
        str += "                                        ";
        str += "                                                    <span class=\"footer1\" style=\"white-space: nowrap;\">";
        str += "                                                        <a href=\"http://nobug.wiki\"";
        str += "                                                            style=\"color: #08c; text-decoration: none;\" rel=\"noopener\"";
        str += "                                                            target=\"_blank\">";
        str += "                                                            NoBug搜索引擎";
        str += "                                                        </a>";
        str += "                                                    </span>";
        str += "                                                    <br>";
        str += "";
        str += "                                                </td>";
        str += "                                            </tr>";
        str += "                                            <tr class=\"footerBottomPadding iPhone_font\"";
        str += "                                                style=\"height: 50px; -premailer-height: 50;\" height=\"50\">";
        str += "                                                <td style=\"font-family: 'Geneva', Helvetica, Arial, sans-serif;\">";
        str += "                                                </td>";
        str += "                                            </tr>";
        str += "                                        </tbody>";
        str += "                                    </table>";
        str += "";
        str += "                                </td>";
        str += "                            </tr>";
        str += "                        </tbody>";
        str += "                    </table>";
        str += "</div>";
        return str;
    }

}
