package io.github.micedre;

import java.util.List;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

public class MailValidatorAuthenticatorFactory implements AuthenticatorFactory {

  @Override
  public Authenticator create(KeycloakSession session) {
    return new MailValidatorAuthenticator();
  }

  @Override
  public void init(Config.Scope config) {
    // NO-OP
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    // NO-OP
  }

  @Override
  public void close() {
    // NO-OP
  }

  @Override
  public String getId() {
    return "mail-validator";
  }

  @Override
  public String getDisplayType() {
    return "Validate mail periodically";
  }

  @Override
  public String getReferenceCategory() {
    return null;
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
    AuthenticationExecutionModel.Requirement.REQUIRED,
    AuthenticationExecutionModel.Requirement.ALTERNATIVE,
    AuthenticationExecutionModel.Requirement.DISABLED
  };

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return REQUIREMENT_CHOICES;
  }

  @Override
  public boolean isUserSetupAllowed() {
    return false;
  }

  @Override
  public String getHelpText() {
    return "Validate mail periodically";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return ProviderConfigurationBuilder.create()
        .property()
        .name("Period")
        .type(ProviderConfigProperty.STRING_TYPE)
        .add()
        .build();
  }
}
