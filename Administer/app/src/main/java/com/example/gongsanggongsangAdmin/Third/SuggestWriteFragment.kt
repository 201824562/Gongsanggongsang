import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsangAdmin.data.CommunityMarketPostModel
import com.example.gongsanggongsangAdmin.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_suggest_write.*
import java.time.LocalDateTime

class SuggestWriteFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_suggest_write, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        community_suggest_write_register_button.setOnClickListener{
            val post = CommunityMarketPostModel(0.toLong(),
                    "주용가리",
                    community_suggest_write_title.text.toString(),
                    community_suggest_write_contents.text.toString(),
                    LocalDateTime.now().toString()
            )

            var documentName = community_suggest_write_title.text.toString() + LocalDateTime.now().toString()
            database.collection("COMMUNITY_Suggest").document(documentName).set(post)
            findNavController().navigate(R.id.communitySuggest)
        }
        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_third) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.third)
    }
}