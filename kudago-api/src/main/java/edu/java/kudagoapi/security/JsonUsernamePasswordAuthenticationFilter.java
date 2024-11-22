package edu.java.kudagoapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.kudagoapi.dtos.user.LoginRequest;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.utils.ExceptionHandlerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import java.util.List;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final LocalValidatorFactoryBean validator;

    public JsonUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper, LocalValidatorFactoryBean validator) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().contains("json")) {
            LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            validateLoginRequest(loginRequest);
            return super.getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getName(), loginRequest.getPassword()));
        }
        throw new AccessDeniedException("Login must be in json format");
    }


    private void validateLoginRequest(LoginRequest loginRequest) {
        DataBinder binder = new DataBinder(loginRequest);
        binder.setValidator(validator);
        binder.validate();
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<String> violatedFields = ExceptionHandlerUtils.getViolatedFields(bindingResult);
            throw new BadRequestApiException(String
                    .format("Invalid request params: %s", violatedFields));
        }
    }
}
