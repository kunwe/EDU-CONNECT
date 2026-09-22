package com.example.educonnect;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class QuizRepository_Factory implements Factory<QuizRepository> {
  private final Provider<GeminiApi> geminiApiProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<FirebaseAuth> authProvider;

  public QuizRepository_Factory(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    this.geminiApiProvider = geminiApiProvider;
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public QuizRepository get() {
    return newInstance(geminiApiProvider.get(), firestoreProvider.get(), authProvider.get());
  }

  public static QuizRepository_Factory create(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    return new QuizRepository_Factory(geminiApiProvider, firestoreProvider, authProvider);
  }

  public static QuizRepository newInstance(GeminiApi geminiApi, FirebaseFirestore firestore,
      FirebaseAuth auth) {
    return new QuizRepository(geminiApi, firestore, auth);
  }
}
