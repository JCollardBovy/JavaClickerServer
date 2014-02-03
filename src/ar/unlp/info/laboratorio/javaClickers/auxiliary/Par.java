package ar.unlp.info.laboratorio.javaClickers.auxiliary;

/**
 * Created with IntelliJ IDEA.
 * User: Jony
 * Date: 02/06/13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class Par<F, C> {

    private F key;
    private C value;

    private Par(){
    }

    public Par(F key, C value) {
        this.key = key;
        this.value = value;
    }

    public F getKey() {
        return key;
    }

    public void setKey(F key) {
        this.key = key;
    }

    public C getValue() {
        return value;
    }

    public void setValue(C value) {
        this.value = value;
    }
}
