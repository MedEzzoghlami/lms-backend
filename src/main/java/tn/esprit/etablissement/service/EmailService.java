package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.dto.TimetableGenerationResult;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTimetableNotification(
            String toEmail,
            String recipientName,
            String weekStart,
            TimetableGenerationResult result) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Timetable ready — week of " + weekStart);
            helper.setText(buildHtmlEmail(
                    recipientName, weekStart, result), true);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }

    private String buildHtmlEmail(String name, String weekStart,
                                  TimetableGenerationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial;max-width:600px;margin:auto;'>");
        sb.append("<div style='background:#0d6efd;padding:20px;border-radius:8px 8px 0 0;'>");
        sb.append("<h2 style='color:white;margin:0;'>LMS — Timetable Ready</h2>");
        sb.append("</div>");
        sb.append("<div style='padding:20px;border:1px solid #e5e7eb;border-top:none;border-radius:0 0 8px 8px;'>");
        sb.append("<p>Hello <strong>").append(name).append("</strong>,</p>");
        sb.append("<p>The timetable for the week of <strong>")
                .append(weekStart).append("</strong> has been generated.</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0;'>");
        sb.append("<thead><tr style='background:#f3f4f6;'>");
        sb.append("<th style='padding:8px;text-align:left;border:1px solid #e5e7eb;'>Course</th>");
        sb.append("<th style='padding:8px;text-align:left;border:1px solid #e5e7eb;'>Date</th>");
        sb.append("<th style='padding:8px;text-align:left;border:1px solid #e5e7eb;'>Time</th>");
        sb.append("<th style='padding:8px;text-align:left;border:1px solid #e5e7eb;'>Room</th>");
        sb.append("<th style='padding:8px;text-align:left;border:1px solid #e5e7eb;'>Class</th>");
        sb.append("</tr></thead><tbody>");

        for (TimetableGenerationResult.ScheduledSlotInfo slot : result.getScheduledSlots()) {
            sb.append("<tr>");
            sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>Course ")
                    .append(slot.getCourseId()).append("</td>");
            sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>")
                    .append(slot.getSlotDate()).append("</td>");
            sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>")
                    .append(slot.getStartTime().substring(0,5))
                    .append(" — ").append(slot.getEndTime().substring(0,5))
                    .append("</td>");
            sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>")
                    .append(slot.getRoomName()).append("</td>");
            sb.append("<td style='padding:8px;border:1px solid #e5e7eb;'>")
                    .append(slot.getClassName()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</tbody></table>");

        if (!result.getSkippedHolidays().isEmpty()) {
            sb.append("<p style='color:#d97706;'>⚠️ Public holidays skipped this week:</p><ul>");
            for (String h : result.getSkippedHolidays()) {
                sb.append("<li>").append(h).append("</li>");
            }
            sb.append("</ul>");
        }

        sb.append("<p style='color:#6b7280;font-size:12px;margin-top:24px;'>")
                .append("This email was sent automatically by the LMS system.</p>");
        sb.append("</div></div>");
        return sb.toString();
    }
}