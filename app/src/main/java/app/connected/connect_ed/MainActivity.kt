package app.connected.connect_ed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.security.GeneralSecurityException
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.util.*
import java.io.*
//
//import java.io.File

class MainActivity : AppCompatActivity() {
    // login vars
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val displayName = findViewById<TextView>(R.id.textViewDisplayName)

        // log in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val google_login_btn = findViewById<SignInButton>(R.id.sign_in_button);
        google_login_btn.setOnClickListener {
            signIn()
        }

        val buttonFlashCards = findViewById(R.id.buttonFlashcards) as Button
        buttonFlashCards.setOnClickListener {
            val intent = Intent(this, FlashcardActivity::class.java)
            startActivity(intent)
        }

        val buttonWordOrder = findViewById(R.id.buttonWordOrder) as Button
        buttonWordOrder.setOnClickListener {
            val intent = Intent(this, WordOrderActivity::class.java)
            startActivity(intent)
        }

        ///TODO:  Sentence completion.  E.g. "Do you like to ____?"
        /// This button appears *only* after the student finishes flashcards
        val buttonDoYouLikeTo = findViewById(R.id.buttonDoYouLikeTo) as Button
        buttonDoYouLikeTo.setOnClickListener {
            val intent = Intent(this, DoYouLikeTo::class.java)
            startActivity(intent)
        }

        val buttonListening = findViewById(R.id.buttonListening) as Button
        buttonListening.setOnClickListener {
            val intent = Intent(this, ListeningActivity::class.java)
            startActivity(intent)
        }
        var newArray = arrayOf("Input")
        DriveQuickstart.main(newArray)
    }

    // function to sign in
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }
    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }

    // called after successful auth
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)
            val displayName = findViewById<TextView>(R.id.textViewDisplayName)
            displayName.text = googleFirstName

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    object DriveQuickstart {
        /** Application name.  */
        private const val APPLICATION_NAME = "Google Drive API Java Quickstart"

        /** Global instance of the JSON factory.  */
        private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

        /** Directory to store authorization tokens for this application.  */
        private const val TOKENS_DIRECTORY_PATH = "tokens"

        /**
         * Global instance of the scopes required by this quickstart.
         * If modifying these scopes, delete your previously saved tokens/ folder.
         */
        private val SCOPES: List<String> =
            Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY)
        private const val CREDENTIALS_FILE_PATH = "/credentials.json"

        /**
         * Creates an authorized Credential object.
         * @param HTTP_TRANSPORT The network HTTP Transport.
         * @return An authorized Credential object.
         * @throws IOException If the credentials.json file cannot be found.
         */
        @Throws(IOException::class)
        private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential {
            // Load client secrets.
            val `in` =
                DriveQuickstart::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                    ?: throw FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH)
            val clientSecrets: GoogleClientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))
            Log.d(clientSecrets.toString(), "client secrets")

            // Build flow and trigger user authorization request.
            val flow: GoogleAuthorizationCodeFlow = GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
            )
                .setDataStoreFactory(FileDataStoreFactory(java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()
            val receiver: LocalServerReceiver = LocalServerReceiver.Builder().setPort(8888).build()
            //returns an authorized Credential object.
            return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
        }

        @Throws(IOException::class, GeneralSecurityException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            // Build a new authorized API client service.
            val HTTP_TRANSPORT: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
            val service: Drive =
                Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build()

            // Print the names and IDs for up to 10 files.
            val result: FileList = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute()
            val files: List<File> = result.getFiles()
            if (files == null || files.isEmpty()) {
                println("No files found.")
            } else {
                println("Files:")
                for (file in files) {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId())
                }
            }
            val fileId = "1iTHLk2umy4s6-1-ktKTmINi_VJGQiAx7"
            val outputStream: OutputStream = ByteArrayOutputStream()
            service.files().get(fileId)
                .executeMediaAndDownloadTo(outputStream)
        }
    }



}