package com.twilio.paymentCenter.jlpsolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/initDB")
public class InitJLPDbController {

    private final SolutionOptionRepository solutionOptionRepository;

    @Autowired
    public InitJLPDbController(final SolutionOptionRepository solutionOptionRepository) {
        this.solutionOptionRepository = solutionOptionRepository;
    }

    @GetMapping
    public void init() {

        this.solutionOptionRepository.save(new SolutionOption("1", "zipCode",
                "Thank you for providing your postal code. For an Electrician press 1. For a Locksmith press 2. ", "init", "trade"));

        String voice = "You selected Electrician! If you need help with your Sockets press 1, if you need help with your Lights press 2.";
        this.solutionOptionRepository.save(new SolutionOption("1", "trade", voice, "zipCode", "job.electrician"));

        voice = "You selected Locksmith! If you need help with your Locks press 1, if you need help with your Smart Lock press 2.";
        this.solutionOptionRepository.save(new SolutionOption("2", "trade", voice, "zipCode", "job.locksmith"));

        voice = "You selected Sockets! If your sockets are faulty press 1, if your sockets need additional installing press 2.";
        this.solutionOptionRepository.save(new SolutionOption("1", "job.sockets", voice, "electrician", "cost"));

        voice = "You selected Lights! If you need downlight installing press 1, if you need outdoor socket installing press 2.";
        this.solutionOptionRepository.save(new SolutionOption("2", "job.lights", voice, "electrician", "cost"));

        voice = "You selected Locks! If you need door handle replacement press 1, if you need door lock replacement press 2.";
        this.solutionOptionRepository.save(new SolutionOption("1", "job.locks", voice, "locksmith", "cost"));

        voice = "You selected Smartlock! If you want to install a Yale keyless smart lock press 1. ";
        this.solutionOptionRepository.save(new SolutionOption("2", "job.smartlock", voice, "locksmith", "cost"));

        voice = "The estimated costs for the solution of your problem are ";
        this.solutionOptionRepository.save(new SolutionOption("1", "cost", voice, "job", "date"));

        voice = "The next available date is on the 35th of December at 3 o clock. Please press 1 if this date and time suits you, any other number if not.";
        this.solutionOptionRepository.save(new SolutionOption("1", "date", voice, "cost", "final"));
    }

}
