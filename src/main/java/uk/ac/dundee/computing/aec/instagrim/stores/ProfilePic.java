package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 * Created by Ryan on 24/10/2015.
 *
 * Store class for a users profile picture, including picture details.
 */
class ProfilePic {

    public ProfilePic() {
    }

    private Pic usersProfilePic;

    public Pic getUsersProfilePic() {
        return usersProfilePic;
    }

    public void setUsersProfilePic(Pic usersProfilePic) {
        this.usersProfilePic = usersProfilePic;
    }
}
