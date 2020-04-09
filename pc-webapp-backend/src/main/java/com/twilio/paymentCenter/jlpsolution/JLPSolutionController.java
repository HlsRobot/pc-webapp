package com.twilio.paymentCenter.jlpsolution;

import com.google.gson.Gson;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("jlp")
public class JLPSolutionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JLPSolutionController.class);

    private final JLPSolutionService jlpSolutionService;

    @Autowired
    public JLPSolutionController(final JLPSolutionService jlpSolutionService) {
        this.jlpSolutionService = jlpSolutionService;
    }

    @PostMapping("initial")
    public void callReceiver(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final String callSid = request.getParameter("CallSid");
        final String caller = request.getParameter("From");
        final VoiceResponse voiceResponse = this.jlpSolutionService.initialCall(callSid, caller);

        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("zipCode")
    public void selectZip(@RequestParam(value = "Digits", required = false) final String digits,
                            final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final String callSid = request.getParameter("CallSid");
        final VoiceResponse voiceResponse = this.jlpSolutionService.handleZip(digits, "init", callSid);
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("trade")
    public void selectTrade(@RequestParam(value = "Digits", required = false) final String digits,
                            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        this.genericSelect(digits, "zipCode", request, response);
    }

    @PostMapping("job.electrician")
    public void selectElJob(@RequestParam(value = "Digits", required = false) final String digits,
                            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        this.genericSelect(digits, "electrician", request, response);
    }

    @PostMapping("job.locksmith")
    public void selectLockJob(@RequestParam(value = "Digits", required = false) final String digits,
                          final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        this.genericSelect(digits, "locksmith", request, response);
    }

    @PostMapping("cost")
    public void selectCost(@RequestParam(value = "Digits", required = false) final String digits,
                            final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        this.genericSelect(digits, "job", request, response);
    }

    @PostMapping("date")
    public void selectDate(@RequestParam(value = "Digits", required = false) final String digits,
                           final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        this.genericSelect(digits, "cost", request, response);
    }

    @PostMapping("final")
    public void selectFinal(@RequestParam(value = "Digits", required = false) final String digits,
                           final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final String callSid = request.getParameter("CallSid");
        final VoiceResponse voiceResponse = this.jlpSolutionService.handleFinal(digits, callSid);

        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    private void genericSelect(final String digits, final String from, final HttpServletRequest request,
                               final HttpServletResponse response) throws IOException {

        final String callSid = request.getParameter("CallSid");
        final VoiceResponse voiceResponse = this.jlpSolutionService.handleAnswers(digits, from, callSid);

        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    @PostMapping("/autopilot")
    public void fromAutoPilot(final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        final Map<String, String[]> parameters = request.getParameterMap();
        LOGGER.info(Arrays.toString(parameters.get("Memory")));
        final HashMap<String, Object> jsonResponse = new HashMap<>()
        {{
            put("actions", new HashMap[] {
                    new HashMap<String, String>()
                    {{
                        put("say", "Thank you for providing the information. An agent will contact you shortly!");
                    }},
            });
        }};
        final Say say = new Say.Builder("Thank you for providing the information. An agent will contact you shortly!").build();
        final VoiceResponse voiceResponse = new VoiceResponse.Builder().say(say).build();
        response.setContentType("application/json");
        response.getWriter().print(new Gson().toJson(jsonResponse));
    }

}
