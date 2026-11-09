import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Theme {

    static final Color DEEP = new Color(46, 16, 101);
    static final Color PURPLE = new Color(124, 58, 237);
    static final Color SOFT = new Color(237, 233, 254);
    static final Color GOLD = new Color(250, 204, 21);
    static final Color PAGE = new Color(250, 248, 255);
    static final Color TEXT = new Color(30, 27, 75);
    static final Color GRAY = new Color(107, 114, 128);

    static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    // Dark purple gradient panel with two soft circles
    static JPanel gradient() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, DEEP, getWidth(), getHeight(), PURPLE));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillOval(getWidth() - 160, -70, 240, 240);
                g2.fillOval(-70, getHeight() - 130, 200, 200);
            }
        };
    }

    // Top banner used in sub windows
    static JPanel header(String title, String subtitle) {
        JPanel p = gradient();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(16, 25, 16, 25));
        JLabel t = new JLabel(title);
        t.setFont(font(Font.BOLD, 26));
        t.setForeground(Color.WHITE);
        JLabel s = new JLabel(subtitle);
        s.setFont(font(Font.PLAIN, 14));
        s.setForeground(GOLD);
        p.add(t);
        p.add(s);
        return p;
    }

    static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(font(Font.BOLD, 13));
        l.setForeground(TEXT);
        return l;
    }

    // Text box with only a purple line at the bottom
    static void field(JTextField f) {
        f.setFont(font(Font.PLAIN, 15));
        f.setForeground(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PURPLE),
                BorderFactory.createEmptyBorder(6, 2, 6, 2)));
    }

    // Rounded button that changes color when the mouse is over it
    static class RoundButton extends JButton {
        Color base;
        boolean hover = false;

        RoundButton(String text, Color bg, Color fg) {
            super(text);
            base = bg;
            setForeground(fg);
            setFont(font(Font.BOLD, 15));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? base.brighter() : base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static JButton button(String text, Color bg, Color fg) {
        return new RoundButton(text, bg, fg);
    }

    // Styled table: dark header, zebra rows, colored status
    static void table(JTable t) {
        t.setRowHeight(36);
        t.setFont(font(Font.PLAIN, 14));
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFillsViewportHeight(true);

        DefaultTableCellRenderer cell = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                setHorizontalAlignment(CENTER);
                setBackground(sel ? PURPLE : (r % 2 == 0 ? Color.WHITE : SOFT));
                setForeground(sel ? Color.WHITE : TEXT);
                String s = (v == null) ? "" : v.toString();
                if (!sel && s.equals("Cancelled")) setForeground(new Color(220, 38, 38));
                if (!sel && s.equals("Booked")) setForeground(new Color(22, 163, 74));
                return this;
            }
        };
        t.setDefaultRenderer(Object.class, cell);

        DefaultTableCellRenderer head = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                setBackground(DEEP);
                setForeground(Color.WHITE);
                setFont(font(Font.BOLD, 14));
                setHorizontalAlignment(CENTER);
                return this;
            }
        };
        t.getTableHeader().setDefaultRenderer(head);
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        t.getTableHeader().setReorderingAllowed(false);
    }
}