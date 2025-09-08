package core;

import java.util.ArrayList;
import java.util.List;

public class HealthManager {
    private int currentHealth;
    private int maxHealth;
    private boolean isDead;
    private List<HealthChangeListener> listeners = new ArrayList<>();

    public static final int DEFAULT_MAX_HEALTH = 5;
    public static final int MIN_HEALTH = 0;

    public interface HealthChangeListener {
        void onHealthChanged(int currentHealth, int maxHealth);
    }

    public HealthManager() {
        this(DEFAULT_MAX_HEALTH);
    }

    public HealthManager(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.isDead = false;
    }


    private void notifyHealthChanged() {
        for (HealthChangeListener listener : listeners) {
            listener.onHealthChanged(currentHealth, maxHealth);
        }
    }

    public boolean takeDamage(int damage) {
        if (isDead || damage <= 0) {
            return isDead;
        }

        currentHealth -= damage;
        System.out.println("💔 Player took " + damage + " damage! Health: " + currentHealth + "/" + maxHealth);
        notifyHealthChanged();

        if (currentHealth <= MIN_HEALTH) {
            currentHealth = MIN_HEALTH;
            isDead = true;
            System.out.println("💀 Player has died!");
            notifyHealthChanged();
            return true;
        }

        return false;
    }


    public void heal(int healAmount) {
        if (isDead || healAmount <= 0) {
            return;
        }

        int oldHealth = currentHealth;
        currentHealth = Math.min(currentHealth + healAmount, maxHealth);

        if (currentHealth > oldHealth) {
            System.out.println("💚 Player healed " + (currentHealth - oldHealth) + " health! Health: " + currentHealth + "/" + maxHealth);
            notifyHealthChanged();
        }
    }


    public void decreaseHealth() {
        if (currentHealth > 0) {
            currentHealth--;
            notifyHealthChanged();
        }
    }

    // Getters
    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isFullHealth() {
        return currentHealth >= maxHealth;
    }


    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
        notifyHealthChanged();
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.max(MIN_HEALTH, Math.min(currentHealth, maxHealth));
        this.isDead = (this.currentHealth == MIN_HEALTH);
        notifyHealthChanged();
    }

    @Override
    public String toString() {
        return currentHealth + "/" + maxHealth + " HP";
    }
}