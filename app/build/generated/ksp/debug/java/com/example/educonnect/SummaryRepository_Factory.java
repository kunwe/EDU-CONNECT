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
public final class SummaryRepository_Factory implements Factory<SummaryRepository> {
  private final Provider<GeminiApi> geminiApiProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  private final Provider<FirebaseAuth> authProvider;

  public SummaryRepository_Factory(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    this.geminiApiProvider = geminiApiProvider;
    this.firestoreProvider = firestoreProvider;
    this.authProvider = authProvider;
  }

  @Override
  public SummaryRepository get() {
    return newInstance(geminiApiProvider.get(), firestoreProvider.get(), authProvider.get());
  }

  public static SummaryRepository_Factory create(Provider<GeminiApi> geminiApiProvider,
      Provider<FirebaseFirestore> firestoreProvider, Provider<FirebaseAuth> authProvider) {
    return new SummaryRepository_Factory(geminiApiProvider, firestoreProvider, authProvider);
  }

  public static SummaryRepository newInstance(GeminiApi geminiApi, FirebaseFirestore firestore,
      FirebaseAuth auth) {
    return new SummaryRepository(geminiApi, firestore, auth);
  }
}
