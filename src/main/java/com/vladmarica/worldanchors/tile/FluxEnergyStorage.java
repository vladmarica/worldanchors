package com.vladmarica.worldanchors.tile;

import com.vladmarica.worldanchors.WorldAnchorsMod;
import net.minecraftforge.energy.IEnergyStorage;

public class FluxEnergyStorage implements IEnergyStorage {
    private final int maxEnergyStored;

    int currentEnergyStored = 0;

    public FluxEnergyStorage(int maxEnergyStored) {
        this.maxEnergyStored = maxEnergyStored;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = Math.min(maxReceive, maxEnergyStored - currentEnergyStored);

        if (!simulate) {
            currentEnergyStored += received;
        }

        WorldAnchorsMod.LOGGER.debug("Recieved {}", received);

        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return currentEnergyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergyStored;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return currentEnergyStored < maxEnergyStored;
    }

    public boolean consume(int amount) {
        if (currentEnergyStored >= amount) {
            currentEnergyStored -= amount;
            return true;
        }

        return false;
    }
}
