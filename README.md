# RPG Project

This is an App for RPG lovers

Create your characters and store their informations, items, weapons, spells, skills, XP control, Dice Roll and more

Also, it has a Tavern page, a kind of RPG Character's social media where you can post, like and comment


### Some Screens Demo


Attributes | Dice Roll | Experience
------------ | ------------- | -------------
<img src="demo/attributes-demo.gif" width="220" height="400"/> | <img src="demo/dice-demo.gif" width="220" height="400"/> | <img src="demo/experience-demo.gif" width="220" height="400"/>


Biography | Inventory | Skills
------------ | ------------- | -------------
<img src="demo/editing-bio-demo.gif" width="220" height="400"/> | <img src="demo/add-item-demo.gif" width="220" height="400"/> | <img src="demo/skill-demo.gif" width="220" height="400"/>


Tavern | Visiting Profile |
------------ | ------------- |
<img src="demo/tavern-demo.gif" width="220" height="400"/> | <img src="demo/visiting-profile-demo.gif" width="220" height="400"/> |

>(All those shown images are ONLY for demo purpose, you may replace them whenever you want)



## Getting Started

Download or clone the App

**You can install the RPG-DEMO.apk to test it**

Then, enjoy it with your friends!

The App has a default Firebase Project associated to it

For a better experience and control, I strongly recommend you replace with your own Firebase Project and custom everything as you wish

The App was made with MVP architeture




### Prerequisites

* Android 5.0 Lollipop (API 21)
* [Android Studio](https://developer.android.com/studio)
* [Realtime Database](https://firebase.google.com/docs/database/android/start)
* [Storage](https://firebase.google.com/docs/storage/android/start)





### Installing and Customizing

* Follow the Firebase implementation guide

* Then you can custom the nodes in class

```java
StringNodes.java
public static final String NODE_CHARACTER = "character";
```


* And also, change the path in each of model objects as you want
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




* To allow your users to post and comment, edit this line in `Character.java` class:
```java
this.canPost = true;
```





* Replace with your own background images changing the following drawable

```
\src\main\res\drawable\bg_default
```




* To use the Notification feature, you MUST implement Firebase Cloud Messaging, get your Authorization Key and set it in NotificationService interface:
```java
public interface NotificationService {

    @Headers({
           "Authorization:key=YOUR_KEY_HERE",
           "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificationData>saveNotification(@Body NotificationData notificationData);
}
```





Notification Strings:
```xml
<string name="notification_commented_post">%1$s commented your %2$s post</string>
<string name="notification_new_post">%1$s made a new post</string>
```





* The Damage Calculator has a specific formula, custom it in `Damage.java` class and match it with your own RPG rules






### Supported Languages

* **English - US**
* **Portuguese - BR**






## Custom Libraries Credits

* **CircleImageView** - [Henning Dodenhof](https://github.com/hdodenhof)
* **Glide** - [Bump Technologies](https://github.com/bumptech)
* **LikeButton** - [Joel Dean](https://github.com/jd-alexander)
* **Custom Activity On Crash library** - [Eduard Ereza Martínez](https://github.com/Ereza)
* **CircleMenu** - [Ramotion](https://github.com/Ramotion)
* **Retrofit** - [Square](https://square.github.io/retrofit/)






## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.






## License
This project is licensed under the MIT License - see the LICENSE.txt file for details






## Authors

* **Gabriel "L" Kuniyoshi** - *Initial work* - [L](https://github.com/kiraitami)




