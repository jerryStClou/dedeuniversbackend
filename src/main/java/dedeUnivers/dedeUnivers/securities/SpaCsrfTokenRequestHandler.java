package dedeUnivers.dedeUnivers.securities;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.function.Supplier;

public final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {

    private final CsrfTokenRequestHandler plainHandler = new CsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler xorHandler = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        // Toujours utiliser XorCsrfTokenRequestAttributeHandler pour assurer la protection BREACH
        this.xorHandler.handle(request, response, csrfToken);

        // Charger le token différé et le rendre disponible via un cookie
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());

        // Si l'en-tête HTTP contient le token, utiliser CsrfTokenRequestAttributeHandler
        // Sinon, utiliser XorCsrfTokenRequestAttributeHandler pour les autres cas
        return (StringUtils.hasText(headerValue) ?
                this.plainHandler : this.xorHandler).resolveCsrfTokenValue(request, csrfToken);
    }
}
