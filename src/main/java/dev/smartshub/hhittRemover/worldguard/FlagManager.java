package dev.smartshub.hhittRemover.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class FlagManager {


    private StateFlag hhittRemoverFlag;

    /**
     * Register the custom flag.
     */
    public void registerFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // Try to register the flag
            StateFlag flag = new StateFlag("hhitt-remover", true);
            registry.register(flag);
            this.hhittRemoverFlag = flag;
        } catch (FlagConflictException e) {
            // If the flag is already registered, get the existing flag
            Flag<?> existing = registry.get("hhitt-remover");
            if (existing instanceof StateFlag) {
                this.hhittRemoverFlag = (StateFlag) existing;
            }
        }
    }

    /**
     * obtain HHITT_REMOVER_FLAG.
     *
     * @return my flag.
     */
    public StateFlag getHhittRemoverFlag() {
        return this.hhittRemoverFlag;
    }

    /**
     * Checks if the flag is registered.
     *
     * @return true if the flag is registered.
     */
    public boolean isFlagRegistered() {
        return this.hhittRemoverFlag != null;
    }
}

