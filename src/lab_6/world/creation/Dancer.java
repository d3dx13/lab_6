package lab_6.world.creation;

import lab_6.world.state.DynamicsState;
import lab_6.world.state.FeelState;

import java.io.Serializable;
import java.util.Date;
import java.time.Instant;
import java.util.Random;

public class Dancer extends People implements Comparable<Dancer>, Serializable {
    final Random random = new Random();
    /**<p>Дата создания объекта</p>
     */
    public Date birthday;
    /**<p>Качество танца</p>
     */
    protected int danceQuality;
    public int getDanceQuality() {
        return danceQuality;
    }

    /**<p>Конструктор</p>
     * @param newName String : имя
     */
    public Dancer(String newName){
        this(newName, 0);
    }

    /**<p>Конструктор</p>
     * @param newName String : имя
     * @param danceSkill int : умение танцевать
     */
    public Dancer(String newName, int danceSkill){
        super(newName);
        this.danceQuality = danceSkill;
        this.birthday = Date.from(Instant.now());
    }

    /**<p>Танцевать</p>
     */
    public void dance(){
        this.setDynamics(DynamicsState.DANCING);
        this.feel(FeelState.WANT_TO_DANCE);
        int temp = random.nextInt(11) - 5;
        this.danceQuality += temp;
    }

    /**<p>Танцевать ХардБасс</p>
     */
    public void danceHard(){
        this.feel(FeelState.CONFUSED);
        this.setDynamics(DynamicsState.FALLING);
        this.setDynamics(DynamicsState.FALLED);
        this.setDynamics(DynamicsState.DANCING);
        this.feel(FeelState.SURPRISED);
        int temp = random.nextInt(30) - 5;
        this.danceQuality += temp;
    }

    /**<p>Танцевать медленный танец</p>
     */
    public void danceSlow(){
        this.setDynamics(DynamicsState.STANDING);
        this.feel(FeelState.SOFT);
        int temp = random.nextInt(30) - 25;
        this.danceQuality += temp;
    }

    /**<p>Танцевать весёлый танец</p>
     */
    public void danceHappy(){
        this.setDynamics(DynamicsState.DANCING);
        this.feel(FeelState.HAPPY);
        int temp = random.nextInt(5);
        this.danceQuality += temp;
    }

    /**<p>Танцевать грустный танец</p>
     */
    public void danceSad(){
        this.setDynamics(DynamicsState.DANCING);
        this.feel(FeelState.SAD);
        int temp = random.nextInt(5) - 5;
        this.danceQuality += temp;
    }

    /**<p>Установить значение параметра по ключу</p>
     * @param key String : ключ
     * @param value String : значение
     */
    public boolean setParam(String key, String value){
        switch (key){
            case "name":
                this.name = value;
                return true;
            case "danceQuality":
                try {
                    int temp = Integer.parseInt(value);
                    this.danceQuality = temp;
                } catch (NumberFormatException ex){
                    return false;
                }
                return true;
            case "dynamics":
                this.dynamicsStateState = DynamicsState.valueOf(value);
                return true;
            case "feel":
                this.feelState = feelState.valueOf(value);
                return true;
            case "think":
                this.thinkState = thinkState.valueOf(value);
                return true;
            case "position":
                this.positionState = positionState.valueOf(value);
                return true;
            default:
                return false;
        }
    }

    @Override
    public int compareTo(Dancer human) {
        int temp =  human.danceQuality - this.danceQuality;
        if (temp != 0)
            return temp;
        return name.compareTo(human.toString());
    }
    @Override
    public boolean equals(Object obj) {
        return (
                (this.name.equals(((Dancer) obj).name))&&
                (this.danceQuality == ((Dancer) obj).danceQuality) &&
                (this.dynamicsStateState == ((Dancer) obj).dynamicsStateState) &&
                (this.feelState == ((Dancer) obj).feelState) &&
                (this.thinkState == ((Dancer) obj).thinkState) &&
                (this.positionState == ((Dancer) obj).positionState)
        );
    }
}
