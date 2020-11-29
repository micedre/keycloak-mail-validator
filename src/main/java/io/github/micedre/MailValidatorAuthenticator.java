package io.github.micedre;

import java.util.Map;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserModel.RequiredAction;

/** MailValidatorAuthenticator */
public class MailValidatorAuthenticator implements Authenticator {

  public static final String MAIL_VALIDATION_TIMESTAMP_ATTRIBUTE = "lastMailValidationTimestamp";

  @Override
  public void close() {
    // no-op
  }

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    Long periodDays = Long.parseLong(getConfigSettingOrDefault(context, "Period", "365"));
    Long periodMillis = periodDays * 24 * 60 * 60 * 1000;

    UserModel user = context.getUser();
    String lastMailValidationTimestamp =
        user.getFirstAttribute(MAIL_VALIDATION_TIMESTAMP_ATTRIBUTE);
    if (lastMailValidationTimestamp == null) {
      user.addRequiredAction(RequiredAction.VERIFY_EMAIL);
      user.setSingleAttribute(
          MAIL_VALIDATION_TIMESTAMP_ATTRIBUTE, Long.toString(System.currentTimeMillis()));
    } else {
      long lastvalidatesince =
          System.currentTimeMillis() - Long.parseLong(lastMailValidationTimestamp);
      //   System.out.println(
      //       "Mail Last Validated " + (lastvalidatesince / (1000 * 3600 * 24)) + " days ago");
      if (lastvalidatesince > periodMillis) {
        user.addRequiredAction(RequiredAction.VERIFY_EMAIL);
        user.setSingleAttribute(
            MAIL_VALIDATION_TIMESTAMP_ATTRIBUTE, Long.toString(System.currentTimeMillis()));
      }
    }
    context.success();
  }

  private String getConfigSettingOrDefault(
      AuthenticationFlowContext context, String key, String defaultValue) {

    AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();
    if (authenticatorConfig == null) {
      return defaultValue;
    }
    Map<String, String> config = authenticatorConfig.getConfig();
    if (config == null) {
      return defaultValue;
    }
    return config.getOrDefault(key, defaultValue);
  }

  @Override
  public void action(AuthenticationFlowContext context) {
    // no-op
  }

  @Override
  public boolean requiresUser() {
    return true;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    // no-op
  }
}
