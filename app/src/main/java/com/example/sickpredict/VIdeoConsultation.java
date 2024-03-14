package com.example.sickpredict;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

public class VIdeoConsultation extends AppCompatActivity {

    EditText edtUsername;
    Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_consultation);

        edtUsername = findViewById(R.id.edtUsername);
        btnProceed = findViewById(R.id.btnProceed);


        
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                if (!username.isEmpty()){
                    proceedService(username);
                    Intent intent = new Intent(VIdeoConsultation.this,CallingActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }else {
                    return;
                }
            }
        });
    }

    void proceedService(String userID){
        Application application = getApplication();
        long appID = 1684648286 ;
        String appSign = "9476b4814ab95dd6a2d29f102649b69212b64ca059f52f4797351d60fc379ae5";
        String userName = userID;

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        //callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }
}