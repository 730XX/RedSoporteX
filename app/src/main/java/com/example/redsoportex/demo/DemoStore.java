package com.example.redsoportex.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos efímeros para revisar la interfaz: no representan información del servidor.
 */
public final class DemoStore {
    public static final class Item {
        public final String id, category, donor;
        public String title, condition, location, description, imageUrl = "";

        Item(String id, String title, String category, String condition, String location, String donor, String description) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.condition = condition;
            this.location = location;
            this.donor = donor;
            this.description = description;
        }
    }

    public static final class Loan {
        public final String id, itemId, notes;
        public final int days;
        public String status;

        Loan(String id, String itemId, String notes, int days, String status) {
            this.id = id;
            this.itemId = itemId;
            this.notes = notes;
            this.days = days;
            this.status = status;
        }
    }

    public static final class Alert {
        public final String id, category, urgency;
        public final int radius;
        public boolean active = true;

        Alert(String id, String category, String urgency, int radius) {
            this.id = id;
            this.category = category;
            this.urgency = urgency;
            this.radius = radius;
        }
    }

    public static final class Incident {
        public final String loanId, type, description;

        Incident(String loanId, String type, String description) {
            this.loanId = loanId;
            this.type = type;
            this.description = description;
        }
    }

    private final List<Alert> alerts = new ArrayList<>();
    private final List<Incident> incidents = new ArrayList<>();

    public List<Alert> alerts() {
        return new ArrayList<>(alerts);
    }

    public void addAlert(String category, String urgency, int radius) {
        alerts.add(0, new Alert(java.util.UUID.randomUUID().toString(), category, urgency, radius));
    }

    public void cancelAlert(String id) {
        for (Alert a : alerts) if (a.id.equals(id)) a.active = false;
    }

    public List<Incident> incidents() {
        return new ArrayList<>(incidents);
    }

    public void addIncident(String loanId, String type, String description) {
        incidents.add(0, new Incident(loanId, type, description));
    }

    private boolean donorMode = false;

    public boolean isDonorMode() {
        return donorMode;
    }

    public void setDonorMode(boolean value) {
        donorMode = value;
    }

    private static final DemoStore INSTANCE = new DemoStore();
    private final List<Item> items = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    private DemoStore() {
        items.add(new Item("chair", "Silla de ruedas", "Sillas de ruedas", "Buen estado", "Miraflores", "Tú", "Silla ligera en buen estado, lista para ser prestada a quien la necesite."));
        items.add(new Item("walker", "Andador plegable", "Andadores", "Excelente", "San Isidro", "Tú", "Andador plegable en excelente estado para facilitar la movilidad."));
        loans.add(new Loan("example-1", "chair", "Coordinar la entrega en un lugar cercano.", 14, "PENDING"));
        loans.add(new Loan("example-2", "walker", "", 7, "ACTIVE"));
    }

    public static DemoStore get() {
        return INSTANCE;
    }

    public Item addDemoItem(String category, String title, String description, String condition, String imageUrl, String location) {
        Item i = new Item(java.util.UUID.randomUUID().toString(), title, category, condition, location, "Tú", description);
        i.imageUrl = imageUrl;
        items.add(0, i);
        return i;
    }

    public List<Item> donorItems() {
        List<Item> result = new ArrayList<>();
        for (Item i : items) if ("Tú".equals(i.donor)) result.add(i);
        return result;
    }

    public List<Item> items() {
        return new ArrayList<>(items);
    }

    public List<Loan> loans() {
        return new ArrayList<>(loans);
    }

    public Item item(String id) {
        for (Item i : items) if (i.id.equals(id)) return i;
        return null;
    }

    public Loan loan(String id) {
        for (Loan l : loans) if (l.id.equals(id)) return l;
        return null;
    }

    public Loan addLoan(String itemId, int days, String notes) {
        Loan l = new Loan(java.util.UUID.randomUUID().toString(), itemId, notes, days, "PENDING");
        loans.add(0, l);
        return l;
    }
}
