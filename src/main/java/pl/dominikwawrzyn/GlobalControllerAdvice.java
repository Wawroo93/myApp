package pl.dominikwawrzyn;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("username")
    public String getUsername(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else {
            return null;
        }
    }
}