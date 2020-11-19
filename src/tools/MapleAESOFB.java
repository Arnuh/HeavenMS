/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package tools;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MapleAESOFB {
    private byte iv[];
    private Cipher cipher;
    private short mapleVersion;
    private final static SecretKeySpec skey = new SecretKeySpec(
        new byte[]{0x13, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, (byte) 0xB4, 0x00, 0x00, 0x00, 0x1B, 0x00, 0x00, 0x00, 0x0F, 0x00, 0x00, 0x00, 0x33, 0x00, 0x00, 0x00, 0x52, 0x00, 0x00, 0x00}, "AES");

    private static final byte[] funnyBytes = new byte[]{(byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F,
        (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F};

    public MapleAESOFB(byte iv[], short mapleVersion) {
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR " + e);
        } catch (NoSuchPaddingException e) {
            System.out.println("ERROR " + e);
        } catch (InvalidKeyException e) {
            System.out.println("Error initializing the encryption cipher.  Make sure you're using the Unlimited Strength cryptography jar files.");
        }
        this.setIv(iv);
        this.mapleVersion = (short) (((mapleVersion >> 8) & 0xFF) | ((mapleVersion << 8) & 0xFF00));
    }

    private void setIv(byte[] iv) {
        this.iv = iv;
    }

    private static byte[] multiplyBytes(byte[] in, int count, int mul) {
        byte[] ret = new byte[count * mul];
        for (int x = 0; x < count * mul; x++) {
            ret[x] = in[x % count];
        }
        return ret;
    }

    public synchronized byte[] crypt(byte[] data) {
        int remaining = data.length;
        int llength = 0x5B0;
        int start = 0;
        while (remaining > 0) {
            byte[] myIv = multiplyBytes(this.iv, 4, 4);
            if (remaining < llength) {
                llength = remaining;
            }
            for (int x = start; x < (start + llength); x++) {
                if ((x - start) % myIv.length == 0) {
                    try {
                        byte[] newIv = cipher.doFinal(myIv);
                        for (int j = 0; j < myIv.length; j++) {
                            myIv[j] = newIv[j];
                        }
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    }
                }
                data[x] ^= myIv[(x - start) % myIv.length];
            }
            start += llength;
            remaining -= llength;
            llength = 0x5B4;
        }
        updateIv();
        return data;
    }

    private synchronized void updateIv() {
        this.iv = getNewIv(this.iv);
    }

    public byte[] getPacketHeader(int length) {
        int iiv = (iv[3]) & 0xFF;
        iiv |= (iv[2] << 8) & 0xFF00;
        iiv ^= mapleVersion;
        int mlength = ((length << 8) & 0xFF00) | (length >>> 8);
        int xoredIv = iiv ^ mlength;
        byte[] ret = new byte[4];
        ret[0] = (byte) ((iiv >>> 8) & 0xFF);
        ret[1] = (byte) (iiv & 0xFF);
        ret[2] = (byte) ((xoredIv >>> 8) & 0xFF);
        ret[3] = (byte) (xoredIv & 0xFF);
        return ret;
    }

    public static int getPacketLength(int packetHeader) {
        int packetLength = ((packetHeader >>> 16) ^ (packetHeader & 0xFFFF));
        packetLength = ((packetLength << 8) & 0xFF00) | ((packetLength >>> 8) & 0xFF);
        return packetLength;
    }

    public boolean checkPacket(byte[] packet) {
        return ((((packet[0] ^ iv[2]) & 0xFF) == ((mapleVersion >> 8) & 0xFF)) && (((packet[1] ^ iv[3]) & 0xFF) == (mapleVersion & 0xFF)));
    }

    public boolean checkPacket(int packetHeader) {
        byte packetHeaderBuf[] = new byte[2];
        packetHeaderBuf[0] = (byte) ((packetHeader >> 24) & 0xFF);
        packetHeaderBuf[1] = (byte) ((packetHeader >> 16) & 0xFF);
        return checkPacket(packetHeaderBuf);
    }

    public static byte[] getNewIv(byte oldIv[]) {
        byte[] in = {(byte) 0xf2, 0x53, (byte) 0x50, (byte) 0xc6};
        for (int x = 0; x < 4; x++) {
            funnyShit(oldIv[x], in);
        }
        return in;
    }

    @Override
    public String toString() {
        return "IV: " + HexTool.toString(this.iv);
    }

    private static byte[] funnyShit(byte inputByte, byte[] in) {
        byte elina = in[1];
        byte anna = inputByte;
        byte moritz = funnyBytes[(int) elina & 0xFF];
        moritz -= inputByte;
        in[0] += moritz;
        moritz = in[2];
        moritz ^= funnyBytes[(int) anna & 0xFF];
        elina -= (int) moritz & 0xFF;
        in[1] = elina;
        elina = in[3];
        moritz = elina;
        elina -= (int) in[0] & 0xFF;
        moritz = funnyBytes[(int) moritz & 0xFF];
        moritz += inputByte;
        moritz ^= in[2];
        in[2] = moritz;
        elina += (int) funnyBytes[(int) anna & 0xFF] & 0xFF;
        in[3] = elina;
        int merry = ((int) in[0]) & 0xFF;
        merry |= (in[1] << 8) & 0xFF00;
        merry |= (in[2] << 16) & 0xFF0000;
        merry |= (in[3] << 24) & 0xFF000000;
        int ret_value = merry;
        ret_value = ret_value >>> 0x1d;
        merry = merry << 3;
        ret_value = ret_value | merry;
        in[0] = (byte) (ret_value & 0xFF);
        in[1] = (byte) ((ret_value >> 8) & 0xFF);
        in[2] = (byte) ((ret_value >> 16) & 0xFF);
        in[3] = (byte) ((ret_value >> 24) & 0xFF);
        return in;
    }
}
