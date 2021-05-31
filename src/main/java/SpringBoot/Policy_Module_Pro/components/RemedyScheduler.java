package SpringBoot.Policy_Module_Pro.components;

import SpringBoot.Policy_Module_Pro.models.Activity;
import SpringBoot.Policy_Module_Pro.models.Policy;
import SpringBoot.Policy_Module_Pro.models.Remedy;
import SpringBoot.Policy_Module_Pro.models.User;
import SpringBoot.Policy_Module_Pro.services.ActivityService;
import SpringBoot.Policy_Module_Pro.services.RemedyService;
import SpringBoot.Policy_Module_Pro.services.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class RemedyScheduler {

    @Autowired
    private RemedyService remedyService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ActivityService activityService;

    private static final Logger log = LoggerFactory.getLogger(RemedyScheduler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate = 60000L) // Remedy Scheduler Running In A 1 Minute Loop
    @Transactional
    public void remedySchedule() throws IOException {

        LocalDateTime logTime = LocalDateTime.now();
        log.info("Remedy Scheduler Log Time : {}", dateFormat.format(new Date()));

        LocalDateTime remedyStartTime = logTime.minusMinutes(1);
        LocalDateTime remedyEndTIme = logTime;

        // We Are Fetching Remedies In Last One Minute As The Older Ones Must Be Handled In Previous Scheduler Runs
        // Still Instead If We Wish We Can Make It Fetch All Remedies Before EndTime !!
        List<Remedy> currentRemedies = remedyService.getRemedyByStatusAndTime(false, remedyStartTime, remedyEndTIme);

        for(Remedy currentRemedy : currentRemedies) {
            Activity currentActivity = activityService.getActivityById(currentRemedy.getId());
            String remedyType = currentRemedy.getRemedyType();
            String fileName = currentActivity.getFileName();
            User user = currentActivity.getUser();
            String userName = user.getUsername();

            if(remedyType.equals("unknown") || remedyType.equals("notify")) {

                Set<Policy> violatedPolicies = currentActivity.getPoliciesViolated();

                StringBuilder userEmailText = new StringBuilder();
                userEmailText.append("File Name : " + fileName + ", Violated Policies : ");

                for(Policy policy : violatedPolicies) {
                    Boolean sendToAdmin = policy.getNotifyAdmin();
                    Boolean sendToUser = policy.getNotifyUser();
                    if(sendToAdmin) {
                        mailService.sendMail(policy.getAdminEmail(), "File Name : " + fileName + ", Violated The Policy : " + policy.getPolicyName(), policy.getAdminEmailSubject());
                    }
                    if(sendToUser) {
                        userEmailText.append(policy.getPolicyName() + ", ");
                    }
                }
                mailService.sendMail(currentActivity.getUser().getEmail(), userEmailText.toString(), "Policy Violation Alert !");
                String sourcePath = "./files/" + userName + "/remedy/" + fileName;
                String destinationPath = "./files/" + userName + "/notified/" + fileName;

                Path isMoved = Files.move(Paths.get(sourcePath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);

                if(isMoved != null) {
                    log.info("Notified Regarding This File & Moved It To Notified Sub-Folder : " + fileName);
                } else {
                    log.info("Notified Regarding The File But Unable To Move The File : " + fileName);
                }

            } else if(remedyType.equals("delete")) {

                Path path = Paths.get("./files/" + userName + "/remedy/" + fileName);
                try {
                    Files.deleteIfExists(path);
                    log.info("Deleted The File : " + fileName);
                } catch (IOException e) {
                    log.info("Unable To Delete The File : " + fileName);
                    System.out.println("Not Able To Delete File : " + e.getMessage());
                }

            } else if(remedyType.equals("rename")) {

                Path currentDir = Paths.get("./files" + "/" + userName + "/remedy/");
                File userDirectoryPath = new File(String.valueOf(currentDir));

                File file = new File(userDirectoryPath + "/" + fileName);
                File renamedFile = new File(userDirectoryPath + "/" + "renamed" + "/" + "Danger_" + fileName);

                if(file.renameTo(renamedFile)) {
                    log.info("Renamed The File : " + fileName);
                } else {
                    log.info("Unable To Rename File : " + fileName);
                }

            } else if(remedyType.equals("move")) {

                String source = "./files/" + userName + "/remedy/" + fileName;
                String destination = "./files/" + userName + "/moved/" + fileName;

                Path isMoved = Files.move(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);

                if(isMoved != null) {
                    log.info("Moved The File : " + fileName);
                } else {
                    log.info("Unable To Move The File : " + fileName);
                }

            } else {
                log.info("Unknown Remedy Type !? Shouldn't Be Possible !!");
            }

            // Update The Status Of Current Remedy To True
            remedyService.updateRemedyStatusById(currentRemedy.getId());
        }
    }

}
