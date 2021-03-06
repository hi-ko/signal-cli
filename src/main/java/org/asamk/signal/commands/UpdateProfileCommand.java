package org.asamk.signal.commands;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import org.asamk.signal.manager.Manager;
import org.whispersystems.libsignal.util.guava.Optional;

import java.io.File;
import java.io.IOException;

public class UpdateProfileCommand implements LocalCommand {

    @Override
    public void attachToSubparser(final Subparser subparser) {
        final MutuallyExclusiveGroup avatarOptions = subparser.addMutuallyExclusiveGroup();
        avatarOptions.addArgument("--avatar").help("Path to new profile avatar");
        avatarOptions.addArgument("--remove-avatar").action(Arguments.storeTrue());

        subparser.addArgument("--name").required(true).help("New profile name");

        subparser.help("Set a name and avatar image for the user profile");
    }

    @Override
    public int handleCommand(final Namespace ns, final Manager m) {
        String name = ns.getString("name");
        String avatarPath = ns.getString("avatar");
        boolean removeAvatar = ns.getBoolean("remove_avatar");

        try {
            Optional<File> avatarFile = removeAvatar
                    ? Optional.absent()
                    : avatarPath == null ? null : Optional.of(new File(avatarPath));
            m.setProfile(name, avatarFile);
        } catch (IOException e) {
            System.err.println("UpdateAccount error: " + e.getMessage());
            return 3;
        }

        return 0;
    }
}
