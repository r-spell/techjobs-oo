package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.data.JobFieldData;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {
        ArrayList items;
        Job someJob = jobData.findById(id);
        model.addAttribute("job", someJob);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid JobForm jobForm, Errors errors, Model model) {
            if (errors.hasErrors()) {
                return "new-job";
            }
            String jobName = jobForm.getName();
            Employer jobEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
            Location jobLocation = jobData.getLocations().findById(jobForm.getLocationId());
            CoreCompetency jobSkill = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());
            PositionType jobPosition = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
            Job newJob = new Job(jobName, jobEmployer, jobLocation, jobPosition, jobSkill);
            jobData.add(newJob);
            int jobId = newJob.getId();
            model.addAttribute("job",newJob);
            return "redirect:/job?id="+jobId;
    }
}
