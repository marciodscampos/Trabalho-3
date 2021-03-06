package library.books;

import gui.MainWindowController;
import library.BorrowFile;
import library.Register;
import library.ReturnFile;
import library.users.User;

import java.util.*;

/**
 * Created by Marcio on 24/05/2015.
 */
public abstract class Book {
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int edition;
    private long bookNumber;
    private boolean available;
    private GregorianCalendar borrowDate;
    private GregorianCalendar returnDate;

    public Book(String title, String author, String publisher, int year, int edition, long bookNumber) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.edition = edition;
        this.bookNumber = bookNumber;
        this.available = true;
        this.borrowDate = null;
        this.returnDate = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getEdition() {
        return edition;
    }

    public void setBookNumber(long bookNumber) {
        this.bookNumber = bookNumber;
    }

    public long getBookNumber() {
        return bookNumber;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isAvailable(BorrowFile borrowFile, ReturnFile returnFile) {
        this.setAvailable(true);

        // Cria as listas de empr�stimos e devolu��es
        ArrayList<Register> borrows = borrowFile.toRegisters();
        ArrayList<Register> returns = returnFile.toRegisters();

        // Verifica se algum livro j� foi emprestado
        if (borrows.isEmpty())
            return true;

        // Ordena as listas
        Collections.sort(borrows);
        Collections.sort(returns);

        // Verfica a disponibilidade do livro
        for (int i = 0; i < borrows.size(); i++) {
            if (borrows.get(i).getBookId() == getBookNumber()) {
                this.setAvailable(false);
                for (int j = 0; j < returns.size(); j++)
                    if (returns.get(j).getBookId() == getBookNumber() && returns.get(j).getCalendar().compareTo(borrows.get(i).getCalendar()) >= 0)
                        this.setAvailable(true);
            }
        }
        return isAvailable();
    }

    public void setReturnDate(GregorianCalendar returnDate) {
        this.returnDate = returnDate;
    }

    public GregorianCalendar getReturnDate() {
        return returnDate;
    }

    public void setBorrowDate(GregorianCalendar borrowDate) {
        this.borrowDate = borrowDate;
    }

    public GregorianCalendar getBorrowDate() {
        return borrowDate;
    }

    public abstract String getBookType();

    public boolean isLate(GregorianCalendar realReturn, User user) {
        // Se o prazo de entrega � menor do que a data de entrega
        if ( this.getReturnDate().compareTo(realReturn) < 0) {
            // Calcula data de desbloqueio
            int nDays = (int) ((this.getReturnDate().getTimeInMillis() - realReturn.getTimeInMillis()) / (1000 * 60 * 60 * 24));
            System.out.println(nDays);

            // Salva a data de desbloqueio
            GregorianCalendar unblockDate = (GregorianCalendar) realReturn.clone();
            unblockDate.add(Calendar.DATE, nDays);
            user.updateUnblockDate(unblockDate);
            return true;
        }
        return false;
    }
}
