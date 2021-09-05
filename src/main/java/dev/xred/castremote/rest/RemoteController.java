package dev.xred.castremote.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import su.litvak.chromecast.api.v2.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/remote")
public class RemoteController {

    private ChromeCast cDevice;

    RemoteController(){
        List<ChromeCast> devices = ChromeCasts.get();

        if(!devices.isEmpty()){
            cDevice = devices.get(0);
            try {
                cDevice.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("connect")
    public ResponseEntity connect(@RequestBody String params) throws GeneralSecurityException, IOException {
        System.out.println("Try connect to" + params);
        cDevice = new ChromeCast(params);
        cDevice.connect();
        System.out.println("Succesfuly connected");
        return ResponseEntity.ok().build();
    }

    @GetMapping("device-list")
    public List<ChromeCast> deviceList() {
        return ChromeCasts.get();
    }

    @GetMapping("discover")
    public ResponseEntity discover() throws IOException, InterruptedException {
        ChromeCasts.startDiscovery();
        while(ChromeCasts.get().isEmpty()) {
            Thread.sleep(1000);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("status")
    public Status  status() throws IOException {
        checkConnection();
        return cDevice.getStatus();
    }

    @GetMapping("mediaStatus")
    public MediaStatus mediaStatus() throws IOException {
        checkConnection();
        return cDevice.getMediaStatus();
    }

    @GetMapping("vol-up")
    public ResponseEntity volUp() throws IOException {
        checkConnection();

        cDevice.setVolume(cDevice.getStatus().volume.level + 0.01f);
        return ResponseEntity.ok().build();
    }

    @GetMapping("vol-d")
    public ResponseEntity volDown() throws IOException {
        checkConnection();

        cDevice.setVolume(cDevice.getStatus().volume.level - 0.01f);
        return ResponseEntity.ok().build();
    }

    @PostMapping("vol-set")
    public ResponseEntity volSet(@RequestBody String vol) throws IOException {
        checkConnection();
        float f = Float.parseFloat(vol);
        if (f < 0 && f > 1)
            return ResponseEntity.internalServerError().build();

        cDevice.setVolume(f);
        return ResponseEntity.ok().build();
    }

    private void checkConnection(){
        if(!cDevice.isConnected()){
            try {
                cDevice.connect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }
}
