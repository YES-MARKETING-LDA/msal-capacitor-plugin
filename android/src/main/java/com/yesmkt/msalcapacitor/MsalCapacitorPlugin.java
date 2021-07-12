package com.yesmkt.msalcapacitor;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IMultipleAccountPublicClientApplication;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.PublicClientApplicationConfiguration;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;

import java.util.List;

//@CapacitorPlugin(name = "MsalCapacitor")
@NativePlugin()
public class MsalCapacitorPlugin extends Plugin {

    private MsalCapacitor implementation = new MsalCapacitor();

    /* Azure AD Variables */
    private IMultipleAccountPublicClientApplication mMultipleAccountApp;
    private List<IAccount> accountList;
    /* Azure AD Variables */
    private IPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    private String[] scopes = {"User.Read"};
    private String idToken = null;

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void loginWithMsAD(PluginCall call) {
        String tenantId = call.getString("tenant");
        String clientId = call.getString("client");
        String scope = call.getString("scope");
        String redirect = call.getString("redirect");

        saveCall(call);

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createMultipleAccountPublicClientApplication(
            getContext(),
            R.raw.auth,
            new IPublicClientApplication.IMultipleAccountApplicationCreatedListener() {
                @Override
                public void onCreated(IMultipleAccountPublicClientApplication application) {
                    Log.d("YESMSAL", "### Cria instancia");
                    mMultipleAccountApp = application;
                    mMultipleAccountApp.acquireToken(getActivity(), scopes, getAuthInteractiveCallback());
                }

                @Override
                public void onError(MsalException exception) {
                    Log.d("YESMSAL", "### ERRO " + exception.toString());
                    PluginCall savedCall = getSavedCall();
                    savedCall.reject(exception.toString());
                }
            });

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        /*PublicClientApplication.create(getContext(),
            R.raw.auth,
            new IPublicClientApplication.ApplicationCreatedListener() {
                @Override
                public void onCreated(IPublicClientApplication application) {
                    Log.d("YESMSAL", "### Cria instancia");
                    mSingleAccountApp = application;
                    mSingleAccountApp.acquireToken(getActivity(), scopes, getAuthInteractiveCallback());
                }

                @Override
                public void onError(MsalException exception) {
                    Log.d("YESMSAL", "### ERRO " + exception.toString());
                    PluginCall savedCall = getSavedCall();
                    savedCall.reject(exception.toString());
                }
            }
            *//*new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                @Override
                public void onCreated(ISingleAccountPublicClientApplication application) {
                    Log.d("YESMSAL", "### Cria instancia");
                    mSingleAccountApp = application;
                    mSingleAccountApp.signIn(getActivity(), null, scopes, getAuthInteractiveCallback());
                }

                @Override
                public void onError(MsalException exception) {
                    Log.d("YESMSAL", "### ERRO " + exception.toString());
                    PluginCall savedCall = getSavedCall();
                    savedCall.reject(exception.toString());
                }
            }*//*);*/
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                /*Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));*/

                /* call graph */
                /*callGraphAPI(authenticationResult);*/

                /* Reload account asynchronously to get the up-to-date list. */
                /*loadAccounts();*/
                idToken = authenticationResult.getAccessToken();
                Log.d("YESMSAL", "TOKEN " + idToken);
                PluginCall savedCall = getSavedCall();
                JSObject ret = new JSObject();
                ret.put("id_token", idToken);
                savedCall.success(ret);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("YESMSAL", "Authentication failed: " + exception.toString());
                /*displayError(exception);*/

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }

                PluginCall savedCall = getSavedCall();
                savedCall.reject(exception.toString());
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d("YESMSAL", "User cancelled login.");
                PluginCall savedCall = getSavedCall();
                savedCall.reject("canceled");
            }
        };
    }
}
