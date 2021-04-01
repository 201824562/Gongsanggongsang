import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gongsanggongsang.data.CommunityMarketPostModel
import com.example.gongsanggongsang.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_community_market_write.*

class MarketWriteFragment : Fragment() {
    var database = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_community_market_write, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        community_market_write_register_button.setOnClickListener{
            val post = CommunityMarketPostModel(0.toLong(),"주용가리",community_market_write_title.text.toString(),community_market_write_contents.text.toString())
            database.collection("COMMUNITY_Market").add(post)
            findNavController().navigate(R.id.communityMarket)
        }
        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_third) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.third)
    }
}