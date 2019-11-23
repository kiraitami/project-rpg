# RPG Project

This is an App for RPG lovers

Create your characters and store their informations, items, weapons, spells, skills, XP control, Dice Roll and more

Also, it has a Tavern page, a kind of RPG Character's social media where you can post, like and comment



## Getting Started

Download or clone the App

Them, enjoy it with your friends!

The App has a default Firebase Project associated to it

For a better experience and control, I recommend you replace with your own Firebase Project and custom everything that you want

The App was made with MVP architeture


### Prerequisites

* [Realtime Database](https://firebase.google.com/docs/database/android/start)
* [Storage](https://firebase.google.com/docs/storage/android/start)


### Installing and Customizing

Follow the Firebase implementation guide
Them you can custom the nodes in class

```java
StringNodes.java
public static final String NODE_CHARACTER = "character";
```


And also change the path in each of model objects
```java
@Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

        databaseReference
                .child(NODE_CHARACTER)
                .child(this.userId)
                .child(generateId())
                .setValue(this);
    }
```

To allow your users to post and comment don't forget to change the line to:
```java
this.canPost = true; //-- default: false
```


Replace with your own background images changing the following drawable

```
\src\main\res\drawable\bg_01
```


The Damage Calculator has a specific formula which is:

Physical Damage:
```java
	int restrictedConst = constitution;
        if (restrictedConst > 80)
            restrictedConst = 80;

        int firstDamageLayer = (int) ((damage - armor) - (constitution /2));
        double constPercentage = (1- (double) restrictedConst/100);

        if (isPercentage) {
            double modifierCalc = (1 + ((double) modifier / 100));
            return  (int) ( (firstDamageLayer * constPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * constPercentage) + modifier );
        }
```

Magic Damage:
```java
	int restrictedMr = mr;
        if (restrictedMr > 60)
            restrictedMr = 60;

        int firstDamageLayer = (int) ((damage - mr) - (mr /2));
        double mrPercentage = (1- (double) restrictedMr/100);

        if (isPercentage) {
            double modifierCalc = (1 + ((double) modifier / 100));
            return  (int) ( (firstDamageLayer * mrPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * mrPercentage) + modifier );
        }
```

Hybrid Damage:
```java
	double armorMr = (armor + mr)/2;
        int restrictedConst = constitution;
        int restrictedMr = mr;
        int restrictedConsMR;

        if (restrictedConst > 80)
            restrictedConst = 80;

        if (restrictedMr > 60)
            restrictedMr = 60;

        restrictedConsMR = (int)((restrictedConst + restrictedMr) /2);


        int firstDamageLayer = (int) ((damage - armorMr) - (constitution /4));
        double consMRPercentage = (1- (double) restrictedConsMR/100);

        if (isPercentage) {
            double modifierCalc = (1 + ((double) modifier / 100));

            return  (int) ( (firstDamageLayer * consMRPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * consMRPercentage) + modifier );
        }
```

### Languages

* **English - US**
* **Portuguese - BR**


## Custom Lybraries Credits

* **CircleImageView** - [Henning Dodenhof](https://github.com/hdodenhof)
* **Glide** - [Bump Technologies](https://github.com/bumptech)
* **LikeButton** - [Joel Dean](https://github.com/jd-alexander)
* **Custom Activity On Crash library** - [Eduard Ereza Martínez](https://github.com/Ereza)
* **CircleMenu** - [Ramotion](https://github.com/Ramotion)



## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.



## License
This project is licensed under the MIT License - see the LICENSE.txt file for details



## Authors

* **Gabriel "L" Kuniyoshi** - *Initial work* - [L](https://github.com/kiraitami)




