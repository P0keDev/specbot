package org.spacehq.mc.protocol.data.game.entity.attribute;

import org.spacehq.mc.protocol.data.MagicValues;

import java.util.UUID;

public class AttributeModifier {

    private ModifierType type;
    private UUID uuid;
    private double amount;
    private ModifierOperation operation;

    public AttributeModifier(ModifierType type, double amount, ModifierOperation operation) {
        if(type == ModifierType.DYNAMIC) {
            throw new IllegalArgumentException("Cannot create a dynamic-typed modifier without a UUID.");
        }

        this.type = type;
        this.uuid = MagicValues.value(UUID.class, type);
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeModifier(UUID uuid, double amount, ModifierOperation operation) {
        try {
            this.type = MagicValues.key(ModifierType.class, uuid);
        } catch(IllegalArgumentException e) {
            this.type = ModifierType.DYNAMIC;
        }

        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }

    public ModifierType getType() {
        return this.type;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public double getAmount() {
        return this.amount;
    }

    public ModifierOperation getOperation() {
        return this.operation;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        AttributeModifier that = (AttributeModifier) o;

        if(Double.compare(that.amount, amount) != 0) return false;
        if(operation != that.operation) return false;
        if(!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        long temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + operation.hashCode();
        return result;
    }

}
