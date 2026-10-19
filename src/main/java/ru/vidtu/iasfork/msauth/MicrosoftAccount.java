package ru.vidtu.iasfork.msauth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import the_fireplace.iasencrypt.EncryptionTools;

public class MicrosoftAccount implements Account, Serializable {
	private static final long serialVersionUID = 5836857834701515666L;
	public static transient ArrayList<MicrosoftAccount> msaccounts = new ArrayList<MicrosoftAccount>();
	public String username;
	public String accessToken;
	public String refreshToken;

	public MicrosoftAccount(String name, String token, String refresh) {
		this.username = name;
		this.accessToken = token;
		this.refreshToken = refresh;
	}

	public static void load(Minecraft mc) {
		try {
			File oldData = new File(mc.mcDataDir, ".iasms");
			if (oldData.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(oldData));
				try {
					msaccounts.add((MicrosoftAccount) ois.readObject());
				} finally {
					ois.close();
				}
				oldData.delete();
			}
		} catch (Throwable t) {
			System.err.println("Unable to load old microsoft accounts.");
			t.printStackTrace();
		}

		try {
			File ms = new File(mc.mcDataDir, ".iasms_v2");
			if (ms.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ms));
				try {
					int amount = ois.readInt();

					for (int i = 0; i < amount; ++i) {
						msaccounts.add((MicrosoftAccount) ois.readObject());
					}
				} finally {
					ois.close();
				}
			}
		} catch (Throwable t) {
			System.err.println("Unable to load microsoft accounts.");
			t.printStackTrace();
		}
	}

	public static void save(Minecraft mc) {
		try {
			File ms = new File(mc.mcDataDir, ".iasms_v2");

			try {
				Files.setAttribute(ms.toPath(), "dos:hidden", false);
			} catch (Throwable ignored) {
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ms));
			try {
				oos.writeInt(msaccounts.size());

				for (MicrosoftAccount acc : msaccounts) {
					oos.writeObject(acc);
				}
			} finally {
				oos.close();
			}

			try {
				Files.setAttribute(ms.toPath(), "dos:hidden", true);
			} catch (Throwable ignored) {
			}
		} catch (Throwable t) {
			System.err.println("Unable to save microsoft accounts.");
			t.printStackTrace();
		}
	}

	@Override
	public String alias() {
		return this.username;
	}

	@Override
	public Throwable login() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.displayGuiScreen(new MSAuthScreen(mc.currentScreen, EncryptionTools.decode(this.accessToken), EncryptionTools.decode(this.refreshToken)));
		return null;
	}
}
