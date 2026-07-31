package alix.common.database;

import alix.common.data.AuthSetting;
import alix.common.data.Identity;
import alix.common.data.LoginType;
import alix.common.data.PersistentUserData;
import alix.common.data.premium.PremiumData;
import alix.common.data.security.password.Password;
import alix.common.database.connect.DatabaseType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

final class NoOPDatabaseImpl implements DatabaseUpdater {
    @Override
    public CompletableFuture<Void> loadAllUsers(Map<String, PersistentUserData> map) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void connect() {

    }

    @Override
    public void saveData(PersistentUserData data) {

    }

    @Override
    public void createTablesSync() {

    }

    @Override
    public void clearPasswordPointers(String name) {

    }

    @Override
    public void updateLastSuccessfulLoginByName(String name, long lastSuccessfulLogin) {

    }

    @Override
    public void updateIpByName(String name, String ip) {

    }

    @Override
    public void updatePasswordByOwner(String ownerName, Password password) {

    }

    @Override
    public void setPremiumData(String name, PremiumData data) {

    }

    @Override
    public void setPassword(String name, Password newPass, boolean isMain) {

    }

    @Override
    public void saveUserToken(Identity identity, String token) {

    }

    @Override
    public PersistentUserData loadUser(String name) {
        return null;
    }

    @Override
    public void updateAuthSettingsByName(String name, AuthSetting authSettings) {

    }

    @Override
    public void updateHasProvenAuthAccessByName(String name, boolean hasProvenAuthAccess) {

    }

    @Override
    public void updateIpAutoLoginByName(String name, Boolean ipAutoLogin) {

    }

    @Override
    public void updateLoginTypeByName(String name, LoginType loginType) {

    }

    @Override
    public void updateExtraLoginTypeByName(String name, LoginType extraLoginType) {

    }

    @Override
    public void updateEmailByName(String name, String email) {

    }

    @Override
    public void removeByName(String name) {

    }

    @Override
    public DatabaseType getType() {
        return null;
    }
}