package com.twilio.paymentCenter.jlpsolution;

import com.twilio.paymentCenter.TwilioConfiguration;
import com.twilio.paymentCenter.jlpsolution.enums.JobEnum;
import com.twilio.paymentCenter.jlpsolution.enums.TradeEnum;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class JLPSolutionService {

    private final TwilioConfiguration twilioConfiguration;

    private final SolutionRepository solutionRepository;

    private final SolutionOptionRepository solutionOptionRepository;

    @Autowired
    public JLPSolutionService(final TwilioConfiguration twilioConfiguration, final SolutionRepository solutionRepository,
                              final SolutionOptionRepository solutionOptionRepository) {
        this.twilioConfiguration = twilioConfiguration;
        this.solutionRepository = solutionRepository;
        this.solutionOptionRepository = solutionOptionRepository;
    }

    public VoiceResponse initialCall(final String callSid, final String phone) {


        //TODO set the correct ngrok address in the Phone Numbers of twilio manager
//        Twilio.init(this.twilioConfiguration.getAccountSid(), this.twilioConfiguration.getAuthToken());

        this.solutionRepository.save(new Solution(callSid, phone));

        final Say say = new Say.Builder("Welcome to Home Solutions from John Lewis. Please provide your 6 digit postal code.").build();
        return new VoiceResponse.Builder().gather(new Gather.Builder().numDigits(6).say(say).action("/jlp/zipCode").build()).build();
    }

    public VoiceResponse handleZip(final String digits, final String previous, final String callSid) {
        final Solution solution = this.solutionRepository.findByCallSid(callSid).orElseThrow(NoSuchElementException::new);
        solution.setZipCode(digits);
        this.solutionRepository.save(solution);
        return this.handleAnswers("1", previous, callSid);
    }

    public VoiceResponse handleAnswers(final String digits, final String previous, final String callSid) {

        if (digits != null) {
            final Solution solution = this.solutionRepository.findByCallSid(callSid).orElseThrow(NoSuchElementException::new);
            final SolutionOption option = this.solutionOptionRepository.findByPreviousAndPhoneId(previous, digits).orElseThrow(NoSuchElementException::new);
            switch (option.getType()) {
                case "trade":
                    solution.setTrade(digits);
                    break;
                case "job":
                    solution.setJob(digits);
                    break;
                case "cost":
                    return this.handleCost(solution, option);
                default:
                    break;
            }
            this.solutionRepository.save(solution);
            final Say say = new Say.Builder(option.getVoice()).build();
            return new VoiceResponse.Builder().gather(new Gather.Builder().numDigits(1).say(say).action("/jlp/" + option.getNext()).build()).build();
        } else {
            if (previous.equals("cost")) {
                final SolutionOption option = this.solutionOptionRepository.findByPrevious(previous).orElseThrow(NoSuchElementException::new);
                final Say say = new Say.Builder(option.getVoice()).build();
                return new VoiceResponse.Builder().gather(new Gather.Builder().numDigits(1).say(say).action("/jlp/" + option.getNext()).build()).build();
            }
        }
        return new VoiceResponse.Builder().say(new Say.Builder("Something went wrong. This call is being terminated").build()).build();
    }

    private VoiceResponse handleCost(final Solution solution, final SolutionOption option) {

        //TODO calculate costs
        final String costs = "45";
        solution.setCost(costs);
        this.solutionRepository.save(solution);

        final Say say = new Say.Builder(String.format("%s %s pounds.", option.getVoice(), costs)).build();
        return new VoiceResponse.Builder().say(say).redirect(new Redirect.Builder("/jlp/" + option.getNext()).build()).build();
    }

    VoiceResponse handleFinal(final String digits, final String callSid) {

        final Solution solution = this.solutionRepository.findByCallSid(callSid).orElseThrow(NoSuchElementException::new);


        final String finalSelection = String.format(
                "You selected %s to fix %s for a cost of %s pounds. ", TradeEnum.getById(solution.getTrade()),
                JobEnum.getByTradeAndId(TradeEnum.getById(solution.getTrade()), solution.getJob()), solution.getCost());

        if (digits != null) {
            Say say;
            if (digits.equals("1")) {
                say = new Say.Builder(finalSelection + "Thank you for using Home Solutions from John Lewis.").build();
            } else {
                say = new Say.Builder("Unfortunately there is no other available date for this kind of service").build();
            }
            return new VoiceResponse.Builder().say(say).build();
        }
        return new VoiceResponse.Builder().say(new Say.Builder("Something went wrong. This call is being terminated").build()).build();
    }
}
