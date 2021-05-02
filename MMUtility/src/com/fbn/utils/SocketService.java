package com.fbn.utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketService implements ConstantsI {
    private final String SocketIP = LoadProp.serverIp;
    private final int SocketPort = Integer.parseInt(LoadProp.socketPort);

    public String executeIntegrationCall(String serviceName, String inputXml){
          String  requestXml = serviceName + "~" + inputXml + "~";
        try
        {
            Socket socket = new Socket(SocketIP, SocketPort);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.write(requestXml.getBytes(StandardCharsets.UTF_16LE));
            dataOutputStream.flush();
            try
            {
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                byte[] readBuffer = new byte[1000000];
                int num = in.read(readBuffer);
                if (num > 0) {
                    byte[] arrayBytes = new byte[num];
                    System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
                  return new String(arrayBytes, StandardCharsets.UTF_16LE);
                }
            } catch (IOException localSocketException)
            {
                localSocketException.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
