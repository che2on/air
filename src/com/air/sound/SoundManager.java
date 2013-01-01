/*
 * Copyright © 2012 Nokia Corporation. All rights reserved.
 * Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation.
 * Oracle and Java are trademarks or registered trademarks of Oracle and/or its
 * affiliates. Other product and company names mentioned herein may be trademarks
 * or trade names of their respective owners.
 * See LICENSE.TXT for license information.
 */

package com.air.sound;

import java.io.InputStream;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

public class SoundManager {

    public static final int SAMPLE_KICK = 0;
    public static final int SAMPLE_SNARE = 1;
    public static final int SAMPLE_COWBELL = 2;
    public static final int SAMPLE_HIHAT_OPEN = 3;
    public static final int SAMPLE_HIHAT_CLOSED = 4;
    public static final int SAMPLE_CRASH = 5;
    public static final int SAMPLE_SPLASH = 6;
    public static final int SAMPLE_RIDE_BELL = 7;
    public static final int SAMPLE_RIDE = 8;
    public static final int SAMPLE_TOM1 = 9;
    public static final int SAMPLE_TOM2 = 10;
    public static final int SAMPLE_TOM3 = 11;
    private static int maxPrefetchedPlayers = 8;
    private static int maxStartedPlayers = supportsMixing() ? 3 : 1;
    private static final Vector prefetchedPlayers = new Vector();
    private static final Vector startedPlayers = new Vector();
    private static final String[] resources = {
        "/oo/burstmusic.mp3", // 0
//        "/snare.wav", // 1
//        "/cowbell.wav", // 2
//        "/hihat2.wav", // 3
//        "/hihat1.wav", // 4
//        "/crash.wav", // 5
//        "/splash.wav", // 6
//        "/ride2.wav", // 7
//        "/ride1.wav", // 8
//        "/tom1.wav", // 9
//        "/tom2.wav", // 10
//        "/tom3.wav" // 11
    };

    /**
     * @return true if mixing is supported
     */
    public static boolean supportsMixing() {
        String s = System.getProperty("supports.mixing");
        return s != null && s.equalsIgnoreCase("true") ;
    }

    /**
     * Play a sound
     * @param sound 
     */
    public  void playSound(int sound) {
        if (restart(sound)) {
            return;
        }

        Player player = null;
        InputStream stream = SoundManager.class.getResourceAsStream(resources[sound]);
        try {
            limitPrefetchedPlayers();
            player = Manager.createPlayer(stream, "audio/mpeg");
            player.realize();
            player.prefetch();
            start(sound, player);
        }
        catch (Exception e) {
            // Either the device is in silent mode...
            if (prefetchedPlayers.isEmpty()) {
            	System.out.println("some problem in playing sound");
              //  Main.getInstance().sampleAlert();
            }
            // ...or does not support having all 8 players prefetched
            else if (maxPrefetchedPlayers > 1) {
                // Reduce amount of players and try again
                maxPrefetchedPlayers--;
                playSound(sound);
            }
        }
    }

    /**
     * Clean up all loaded resources
     */
    public static void cleanUp() {
        synchronized (prefetchedPlayers) {
            while (!prefetchedPlayers.isEmpty()) {
                clean((SoundPlayer) prefetchedPlayers.firstElement());
            }
        }
    }

    /**
     * Deallocate player
     * @param sp
     */
    private static void clean(SoundPlayer sp) {
        synchronized (prefetchedPlayers) {
            prefetchedPlayers.removeElement(sp);
            stop(sp.player);
            try {
                sp.player.deallocate();
                sp.player.close();
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * Restart player, if it's already in prefetched players
     * @param sound
     * @return
     */
    private static boolean restart(int sound) {
        synchronized (prefetchedPlayers) {
            for (int i = 0; i < prefetchedPlayers.size(); i++) {
                SoundPlayer sp = (SoundPlayer) prefetchedPlayers.elementAt(i);
                if (sp.sound == sound) {
                    prefetchedPlayers.removeElement(sp);
                    prefetchedPlayers.addElement(sp);
                    stop(sp.player);
                    try {
                        sp.player.setMediaTime(0);
                    }
                    catch (Exception e) {
                    }
                    start(sp.player);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Limit the amount of prefetched players
     */
    private static void limitPrefetchedPlayers() {
        synchronized (prefetchedPlayers) {
            try {
                while (prefetchedPlayers.size() >= maxPrefetchedPlayers) {
                    SoundPlayer sp = (SoundPlayer) prefetchedPlayers.firstElement();
                    clean(sp);
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * Start a new player
     * @param sound
     * @param player
     */
    private static void start(int sound, Player player) {
        synchronized (prefetchedPlayers) {
            prefetchedPlayers.addElement(new SoundPlayer(sound, player));
            start(player);
        }
    }

    /**
     * Start a
     * @param player
     */
    private static void start(Player player) {
        synchronized (startedPlayers) {
            try {
                while (startedPlayers.size() >= maxStartedPlayers) {
                    Player p = (Player) startedPlayers.firstElement();
                    startedPlayers.removeElementAt(0);
                    stop(p);
                }
            }
            catch (Exception e) {
            }
            startedPlayers.addElement(player);
            try {
                player.start();
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * Stop a player
     * @param player
     */
    private static void stop(Player player) {
        synchronized (startedPlayers) {
            startedPlayers.removeElement(player);
            try {
                if (player.getState() == Player.STARTED) {
                    try {
                        player.stop();
                    }
                    catch (Exception e) {
                    }
                }
            }
            catch (Exception e) {
            }
        }
    }

    /**
     * Associate a sound to a player
     */
    private static class SoundPlayer {

        public final int sound;
        public final Player player;

        public SoundPlayer(int sound, Player player) {
            this.sound = sound;
            this.player = player;
        }
    }
}
